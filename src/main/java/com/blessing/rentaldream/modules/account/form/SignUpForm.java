package com.blessing.rentaldream.modules.account.form;

import com.blessing.rentaldream.infra.config.AnnotationValidatorErrorMessageConfig;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {
    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$",message = AnnotationValidatorErrorMessageConfig.NICKNAME_PATTERN_ERROR_MESSAGE)
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;

    @NotBlank
    @Length(min = 8, max = 50)
    private String confirmPassword;

}
