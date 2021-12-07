package com.blessing.rentaldream.infra.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Component
@Profile("test")
@Slf4j
public class TestFileManager implements FileManager{
    private final static String FILE_UPLOAD_PATH="src/test/resources/image/";

    @Override
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        File savedFile = new File(FILE_UPLOAD_PATH,"test.png");
        try {
            log.info("content-type  " + file.getContentType());
            FileUtils.copyInputStreamToFile(file.getInputStream(), savedFile);
        } catch (IOException e) {
            log.error("Error while create local file.", e);
            throw e;
        }
        return FILE_UPLOAD_PATH + "test.png";
    }

    @Override
    public String deleteFile(String fileName) throws IOException {
        File file = new File(FILE_UPLOAD_PATH+fileName);
        if(existByFileName(fileName)) {
            log.info("파일 존재");
            if (file.delete()) log.info("파일 삭제 성공");
            else log.info("파일 삭제 실패");
        }
        return fileName;
    }

    @Override
    public boolean existByFileName(String fileName) {
        File file = new File(FILE_UPLOAD_PATH+fileName);
        return file.exists();
    }
}
