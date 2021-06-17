package com.blessing.lentaldream.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.blessing.lentaldream.infra.config.AnnotationValidatorErrorMessageConfig.NICKNAME_PATTERN_ERROR_MESSAGE;

@Data
public class ProfileForm {
    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$",message = NICKNAME_PATTERN_ERROR_MESSAGE)
    private String nickname;

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$",message = NICKNAME_PATTERN_ERROR_MESSAGE)
    private String currentNickname;

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    @URL
    private String url;

    @Length(max = 50)
    private String location;

}
