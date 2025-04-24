package domostroy.core.application.cloudStorage;

import domostroy.core.application.cloudStorage.config.YandexCloudProperties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Getter
public class FileStorageService {
    private final S3Client s3Client;
    private final YandexCloudProperties properties;
    private static final long FILE_LINK_DEFAULT_TTL_MINUTES = 15L;

    @PostConstruct
    public void init() {
        if (!checkBucketExistence(properties.getBucket())) {
            log.warn("Storage bucket does not exist! Creating bucket ...");
            try {
                createDefaultBucket();
                log.info("Bucket successfully created!");
            } catch (Exception e) {
                log.error("Error creating bucket: {}", e.getMessage());
                System.exit(1);
            }
        }
    }


    public String getPresignedUrl(String path) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())
                ))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(properties.getBucket())
                    .key(path)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(FILE_LINK_DEFAULT_TTL_MINUTES))
                    .getObjectRequest(getObjectRequest)
                    .build();
            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);
            return presignedGetObjectRequest.url().toString();
        }
    }

    public boolean deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(properties.getBucket())
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (Exception e) {
            log.error("Error deleting file {}: {}", key, e.getMessage());
            return false;
        }
    }


    public boolean deleteFiles(List<String> paths) {
        try {
            List<ObjectIdentifier> objects = paths.stream()
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .collect(Collectors.toList());
            Delete delete = Delete.builder().objects(objects).build();
            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(properties.getBucket())
                    .delete(delete)
                    .build();
            s3Client.deleteObjects(deleteObjectsRequest);
            return true;
        } catch (Exception e) {
            log.error("Error deleting files: {}", e.getMessage());
            return false;
        }
    }


    public String saveFile(InputStream file, String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        try {
            long fileSize = file.available();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(properties.getBucket())
                    .key(path)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file, fileSize));
            return path;
        } catch (Exception e) {
            log.error("Error uploading file {}: {}", path, e.getMessage());
            throw new RuntimeException("Error uploading file", e);
        }
    }


    public Collection<String> saveAllFiles(Collection<InputStream> files, Collection<String> paths) {
        if (paths == null || paths.isEmpty() || files.size() != paths.size()) {
            throw new IllegalArgumentException("File upload error: paths collection is empty or sizes mismatch");
        }
        List<String> uploadedPaths = new ArrayList<>();
        List<InputStream> fileList = new ArrayList<>(files);
        List<String> pathList = new ArrayList<>(paths);
        for (int i = 0; i < fileList.size(); i++) {
            String uploadedKey = saveFile(fileList.get(i), pathList.get(i));
            uploadedPaths.add(uploadedKey);
        }
        return uploadedPaths;
    }


    private boolean checkBucketExistence(String bucketName) {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        } catch (Exception e) {
            log.error("Error checking bucket existence: {}", e.getMessage());
            return false;
        }
    }


    private void createDefaultBucket() {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(properties.getBucket())
                    .createBucketConfiguration(CreateBucketConfiguration.builder()
                            .locationConstraint(properties.getRegion())
                            .build())
                    .build();
            s3Client.createBucket(createBucketRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bucket: " + e.getMessage(), e);
        }
    }
}
