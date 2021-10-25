package com.blessing.rentaldream.modules.post.validator;

import com.blessing.rentaldream.infra.config.ErrorCodeConfig;
import com.blessing.rentaldream.modules.post.form.PostForm;
import com.blessing.rentaldream.modules.tag.TagForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
@RequiredArgsConstructor
public class PostFormValidator implements Validator {
    private final ObjectMapper objectMapper;
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
    }


}
