package com.blessing.lentaldream.modules.account.validator;

import com.blessing.lentaldream.modules.account.form.SignUpForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
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
        checkConfirmPassword(errors,form.getPassword(),form.getConfirmPassword());

    }

    private void checkConfirmPassword(Errors errors, String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            errors.rejectValue("password", "invalid.password","패스워드가 일치하지 않습니다.");
        }
    }

    private void checkNicknameDuplication(Errors errors, String nickname) {
        if (accountRepository.existsByNickname(nickname)){
            errors.rejectValue("nickname", "invalid.nickname","이미 사용중인 닉네임입니다.");
        }
    }

    private void checkEmailDuplication(Errors errors, String email) {
        if (accountRepository.existsByEmail(email)) {
            errors.rejectValue("email", "invalid.email", new Object[]{email}, "이미 사용중인 이메일입니다.");
        }
    }
}
