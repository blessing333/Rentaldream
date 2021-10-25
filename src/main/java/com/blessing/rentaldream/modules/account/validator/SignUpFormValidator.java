package com.blessing.rentaldream.modules.account.validator;

import com.blessing.rentaldream.infra.config.ErrorCodeConfig;
import com.blessing.rentaldream.modules.account.form.SignUpForm;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm form = (SignUpForm) target;
        checkEmailDuplication(errors, form.getEmail());
        checkNicknameDuplication(errors, form.getNickname());
        checkConfirmPasswordNotEqual(errors,form.getPassword(),form.getConfirmPassword(),"confirmPassword");
    }

    public void checkNicknameDuplication(Errors errors, String nickname) {
        if (accountRepository.existsByNickname(nickname)){
            errors.rejectValue("nickname", ErrorCodeConfig.DUPLICATED_NICKNAME_ERROR_CODE);
        }
    }

    public void checkConfirmPasswordNotEqual(Errors errors, String password, String confirmPassword,String errorFiledName) {
        if(!password.equals(confirmPassword)){
            errors.rejectValue(errorFiledName, ErrorCodeConfig.CONFIRM_PASSWORD_NOT_MATCHING_ERROR_CODE);
        }
    }

    private void checkEmailDuplication(Errors errors, String email) {
        if (accountRepository.existsByEmail(email)) {
            errors.rejectValue("email", ErrorCodeConfig.DUPLICATED_EMAIL_ERROR_CODE);
        }
    }
}
