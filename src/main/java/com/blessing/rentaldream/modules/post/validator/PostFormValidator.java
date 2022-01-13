package com.blessing.rentaldream.modules.post.validator;

import com.blessing.rentaldream.infra.config.ErrorCodeConfig;
import com.blessing.rentaldream.modules.post.form.PostForm;
import com.blessing.rentaldream.modules.tag.TagForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostFormValidator implements Validator {
    private static final int MAXIMUM_TAG_COUNT = 3;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PostForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PostForm postForm = (PostForm) target;
        TagForm [] tagArray = postForm.getTagsAsArray();
        if(tagArray!= null && tagArray.length > MAXIMUM_TAG_COUNT){
            errors.rejectValue("tagsWithJsonString", ErrorCodeConfig.MAX_OUT_TAG_COUNT);
        }
        MultipartFile thumbnailFile = postForm.getThumbnail();
        if(postForm.getThumbnailPath() == null && thumbnailFile.isEmpty()){
            log.error("썸네일 없음 ---- " + thumbnailFile.getSize());
            errors.rejectValue("thumbnail", ErrorCodeConfig.NO_THUMBNAIL);
        }


        if(!thumbnailFile.isEmpty()  && !thumbnailFile.getContentType().startsWith("image")){
            log.error("파일 형식 불일치 ---- " +thumbnailFile.getContentType());
            errors.rejectValue("thumbnail","not_image","썸네일은 이미지 파일이어야합니다.");
        }

    }


}
