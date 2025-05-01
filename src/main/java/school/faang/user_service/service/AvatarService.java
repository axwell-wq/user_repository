package school.faang.user_service.service;

import org.springframework.web.multipart.MultipartFile;
import school.faang.user_service.entity.UserProfilePic;

public interface AvatarService {
    UserProfilePic createUserAvatar(MultipartFile file, Long userId);

    UserProfilePic updateUserAvatar(MultipartFile file, Long userId);

    void deleteUserAvatar(Long userId);
}
