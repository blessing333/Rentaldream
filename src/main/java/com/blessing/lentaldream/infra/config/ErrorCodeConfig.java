package com.blessing.lentaldream.infra.config;

public class ErrorCodeConfig {
    //닉네임 관련 에러코드
    public final static String DUPLICATED_NICKNAME_ERROR_CODE ="duplicated.nickname";

    //이메일 관련 에러코드
    public final static String DUPLICATED_EMAIL_ERROR_CODE ="duplicated.email";

    //비밀번호 관련 에러코드
    public final static String INVALID_PASSWORD_ERROR_CODE ="invalid.password";
    public final static String CONFIRM_PASSWORD_NOT_MATCHING_ERROR_CODE ="invalid.confirmPassword";
}