package com.ecom.art_courses.service;

import java.io.File;

public interface S3Service {
    void uploadImage(File file);
}
