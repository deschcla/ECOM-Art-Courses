package com.ecom.art_courses.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3ServiceImpl {

    private final AmazonS3 amazonS3;
    private final String bucketName = "ecom-art-courses-s3";

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void uploadImage(File file) throws IOException {
        //        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
        String key = "images/" + file.getName(); // Specify the S3 object key
        PutObjectRequest request = new PutObjectRequest(bucketName, key, file);
        amazonS3.putObject(request);
    }
}
