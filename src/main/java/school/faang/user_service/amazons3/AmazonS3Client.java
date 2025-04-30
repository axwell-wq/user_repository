package school.faang.user_service.amazons3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Component
public class AmazonS3Client {

    @Value("${s3.endpoint}")
    private String minioUrl;

    @Value("${s3.aws_access_key_id}")
    private String awsAccessKey;

    @Value("${s3.aws_secret_access_key}")
    private String awsSecretKey;

    public S3Client getS3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);

        return S3Client.builder()
                .endpointOverride(URI.create(minioUrl))
                .serviceConfiguration(b -> b.pathStyleAccessEnabled(true))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.US_EAST_1)
                .build();
    }
}
