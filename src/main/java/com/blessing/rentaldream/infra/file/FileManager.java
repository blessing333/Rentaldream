package com.blessing.rentaldream.infra.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManager {
    String uploadFile(MultipartFile file,String fileName) throws IOException;
    String deleteFile(String fileName) throws IOException;
    boolean existByFileName(String fileName);
}
