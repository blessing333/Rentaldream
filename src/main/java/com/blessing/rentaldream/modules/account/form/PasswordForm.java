package com.blessing.rentaldream.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PasswordForm {
    @NotNull
    private Long accountId;

    @NotBlank
    @Length(min = 8, max = 50)
    private String currentPassword;

    @NotBlank
    @Length(min = 8, max = 50)
    private String newPassword;

    @NotBlank
    @Length(min = 8, max = 50)
    private String confirmPassword;
}
