package com.blessing.rentaldream.modules.account.validator;

import com.blessing.rentaldream.modules.account.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProfileFormValidator implements Validator {
    private final SignUpFormValidator signUpFormValidator;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProfileForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileForm profileForm = (ProfileForm)target;
        String currentNickname = profileForm.getCurrentNickname();
        String profileFormNickname = profileForm.getNickname();
        if(!currentNickname.equals(profileFormNickname))
            signUpFormValidator.checkNicknameDuplication(errors,profileForm.getNickname());

    }
}
