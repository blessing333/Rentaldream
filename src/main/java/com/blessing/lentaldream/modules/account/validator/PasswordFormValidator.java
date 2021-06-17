package com.blessing.lentaldream.modules.account.validator;

import com.blessing.lentaldream.infra.config.ErrorCodeConfig;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.form.PasswordForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PasswordFormValidator implements Validator {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;
    private static final String CURRENT_PASSWORD_FIELD_NAME = "currentPassword";
    private static final String NEW_PASSWORD_FIELD_NAME = "newPassword";
    private static final String CONFIRM_NEW_PASSWORD_FIELD_NAME = "confirmPassword";
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm)target;
        checkCurrentPassword(errors,passwordForm.getAccountId(), passwordForm.getCurrentPassword());
        signUpFormValidator.checkConfirmPasswordNotEqual(errors,passwordForm.getNewPassword(), passwordForm.getConfirmPassword(),CONFIRM_NEW_PASSWORD_FIELD_NAME);
    }
    private void checkCurrentPassword(Errors errors, Long accountId,String encodedPassword){
        Account account = accountRepository.findById(accountId).get();

        if(!passwordEncoder.matches(encodedPassword,account.getPassword())){
            errors.rejectValue(CURRENT_PASSWORD_FIELD_NAME, ErrorCodeConfig.INVALID_PASSWORD_ERROR_CODE);
        }
    }
}
