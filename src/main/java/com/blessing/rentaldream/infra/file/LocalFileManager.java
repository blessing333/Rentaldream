package com.blessing.rentaldream.infra.file;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Component
@Profile({"dev"})
@Slf4j
public class LocalFileManager implements FileManager {
    private final static String FILE_UPLOAD_PATH="src/main/resources/static/assets/thumbnails/";
    private final static String FILE_ACCESS_PATH = "/assets/thumbnails/";
    @Override
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        if(!existByFileName(fileName)) {
            File savedFile = new File(FILE_UPLOAD_PATH, fileName);
            try {
                log.info("content-type  " + file.getContentType());
                FileUtils.copyInputStreamToFile(file.getInputStream(), savedFile);
            } catch (IOException e) {
                log.error("Error while create local file.", e);
                throw e;
            }
        }
        return FILE_ACCESS_PATH + fileName;
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
