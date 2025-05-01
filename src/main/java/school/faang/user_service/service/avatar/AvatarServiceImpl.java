package school.faang.user_service.service.avatar;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import school.faang.user_service.amazons3.AmazonS3Client;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.UserProfilePic;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.AvatarService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static school.faang.user_service.config.context.UserAvatarConfig.LARGE_IMAGE_SIZE;
import static school.faang.user_service.config.context.UserAvatarConfig.SMALL_IMAGE_SIZE;
import static school.faang.user_service.config.context.UserAvatarConfig.MAX_FILE_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    @Value("${s3.bucket_name}")
    private String bucketName;

    @Value("${s3.endpoint}")
    private String minioUrl;

    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;

    public UserProfilePic createUserAvatar(MultipartFile file, Long userId) {
        log.info("File size: {} bytes, MAX_FILE_SIZE: {}", file.getSize(), MAX_FILE_SIZE);
        UserProfilePic userPic = uploadAvatar(file, userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        user.setUserProfilePic(userPic);

        userRepository.save(user);
        log.info("Аватар успешно создан");

        return userPic;
    }

    public UserProfilePic updateUserAvatar(MultipartFile file, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getUserProfilePic() != null) {
            deleteOldAvatars(user.getUserProfilePic());
        }

        UserProfilePic newProfilePic = uploadAvatar(file, userId);

        user.setUserProfilePic(newProfilePic);
        userRepository.save(user);
        log.info("Аватар обновлен");

        return newProfilePic;
    }

    public void deleteUserAvatar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getUserProfilePic() != null) {
            deleteOldAvatars(user.getUserProfilePic());
            user.setUserProfilePic(null);
            userRepository.save(user);
            log.info("Аватар успешно удален для пользователя с идентификатором: {}", userId);
        }
    }

    private UserProfilePic uploadAvatar(MultipartFile file, Long userId) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new DataValidationException("File size exceeds 5MB limit");
        }

        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            if (originalImage == null) {
                throw new DataValidationException("Unsupported image format");
            }

            String fileExtension = getFileExtension(file.getOriginalFilename());
            String baseKey = "avatars/user_" + userId + "/" + UUID.randomUUID();

            BufferedImage largeImage = resizeImage(originalImage, LARGE_IMAGE_SIZE);
            BufferedImage smallImage = resizeImage(originalImage, SMALL_IMAGE_SIZE);

            String largeKey = baseKey + "_large" + fileExtension;
            String smallKey = baseKey + "_small" + fileExtension;

            try (S3Client s3Client = amazonS3Client.getS3Client()) {
                uploadImageToS3(s3Client, largeImage, file.getContentType(), largeKey);
                uploadImageToS3(s3Client, smallImage, file.getContentType(), smallKey);

                return new UserProfilePic(largeKey, smallKey);
            }
        } catch (IOException e) {
            log.error("Error processing avatar image", e);
            throw new RuntimeException("Failed to process avatar image", e);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetSize) {
        return Scalr.resize(originalImage,
                Scalr.Method.QUALITY,
                Scalr.Mode.AUTOMATIC,
                targetSize,
                targetSize,
                Scalr.OP_ANTIALIAS);
    }

    private void uploadImageToS3(S3Client s3Client, BufferedImage image, String contentType, String key) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        byte[] bytes = os.toByteArray();

        ensureBucketExists(s3Client);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromBytes(bytes)
        );
    }

    private String generateUrl(S3Client s3Client, String key) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toString();
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        String extension = filename.substring(filename.lastIndexOf("."));
        return extension.toLowerCase().matches("\\.(jpg|jpeg|png|gif)") ? extension : ".jpg";
    }

    private void ensureBucketExists(S3Client s3Client) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    private void deleteOldAvatars(UserProfilePic oldProfilePic) {
        if (oldProfilePic == null || oldProfilePic.getFileId() == null) {
            return;
        }

        try (S3Client s3Client = amazonS3Client.getS3Client()) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(oldProfilePic.getFileId())
                    .build());

            if (oldProfilePic.getSmallFileId() != null) {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(oldProfilePic.getSmallFileId())
                        .build());
            }
        } catch (S3Exception e) {
            log.error("Failed to delete old avatars: {}", e.getMessage());
        }
    }
}
