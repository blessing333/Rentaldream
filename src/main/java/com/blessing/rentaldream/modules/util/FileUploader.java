package com.blessing.rentaldream.modules.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    String uploadFile(MultipartFile file,String fileName) throws IOException;
    String deleteFile(String fileName) throws IOException;
    boolean existByFileName(String fileName);
}
