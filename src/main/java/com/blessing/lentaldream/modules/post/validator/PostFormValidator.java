package com.blessing.lentaldream.modules.post.validator;

import com.blessing.lentaldream.infra.config.ErrorCodeConfig;
import com.blessing.lentaldream.modules.post.form.PostForm;
import com.blessing.lentaldream.modules.tag.TagForm;
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
        if(tagArray.length >= MAXIMUM_TAG_COUNT){
            errors.rejectValue("tagsWithJsonString", ErrorCodeConfig.MAX_OUT_TAG_COUNT);
        }
    }


}
