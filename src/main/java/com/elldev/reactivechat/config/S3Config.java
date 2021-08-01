package com.elldev.reactivechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class S3Config {
    @Value("${s3.bucket-name}")
    private String bucketName;

    @Value("${s3.access-key}")
    private String accessKey;

    @Value("${s3.secret-key}")
    private String secretKey;

    @Value("${s3.session-token:}")
    private String sessionToken;

    @Bean
    public String bucketName() {
        return this.bucketName;
    }

    @Bean
    public StaticCredentialsProvider staticCredentialsProvider() {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);
        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public S3AsyncClient s3AsyncClient(StaticCredentialsProvider credentialsProvider) {
        return S3AsyncClient.builder().region(Region.AP_NORTHEAST_2).credentialsProvider(credentialsProvider).build();
    }
}
