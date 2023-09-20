package com.ecom.art_courses.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
}
