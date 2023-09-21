package com.ecom.art_courses.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class S3ServiceImpl {

    private final AmazonS3 amazonS3;
    private final String bucketName = "ecom-art-courses-s3";

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void uploadImage(File file) throws IOException {
        String key = "images/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String getPresignedURL(String objectKey) {
        // Define the expiration time for the presigned URL (e.g., 1 hour from now)
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 360 * 60 * 1000; // 6 hours in milliseconds
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
