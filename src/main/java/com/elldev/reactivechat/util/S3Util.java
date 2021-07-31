package com.elldev.reactivechat.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class S3Util {
    private final S3AsyncClient s3;
    private final StaticCredentialsProvider credentialsProvider;

    public List<S3Object> getS3Objects(String bucketName, String key) throws ExecutionException, InterruptedException {
        ListObjectsRequest listObjects = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(key).build();
        return s3.listObjects(listObjects).get().contents();
    }

    public URL getFileUrl(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key).build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest).build();

        S3Presigner preSigner = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.AP_NORTHEAST_2).build();

        PresignedGetObjectRequest presignedGetObjectRequest = preSigner.presignGetObject(getObjectPresignRequest);
        preSigner.close();

        return presignedGetObjectRequest.url();
    }

    public URL saveFile(MultipartFile file, String bucketName, String key) throws IOException {
        s3.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .key(key).build(), AsyncRequestBody.fromBytes(file.getBytes()));

        GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(key).build();
        return s3.utilities().getUrl(request);
    }

    public void deleteFile(String bucketName, String key) {
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key).build());
    }
}
