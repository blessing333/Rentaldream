package com.blessing.lentaldream.modules.account.controller;

import com.blessing.lentaldream.infra.config.UrlConfig;
import com.blessing.lentaldream.modules.account.AccountFactory;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountFactory accountFactory;

    private final String TEST_NICKNAME = "testname";
    private final String TEST_ID = "testEmail@email.com";
    private final String TEST_PASSWORD = "testPassword";

    @BeforeEach
    public void init() throws Exception{
        mockMvc.perform(post(UrlConfig.SIGN_UP_URL)
                .param("nickname",TEST_NICKNAME)
                .param("email",TEST_ID)
                .param("password",TEST_PASSWORD)
                .param("confirmPassword",TEST_PASSWORD)
                .with(csrf())
        );
    }

    @DisplayName("회원 가입 화면 생성 테스트")
    @Test
    public void createSignUpView() throws Exception{
        mockMvc.perform(get(UrlConfig.SIGN_UP_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원 가입 실패 - 중복 이메일 존재")
    @Test
    public void signUpWithDuplicatedEmail() throws Exception{
        mockMvc.perform(post(UrlConfig.SIGN_UP_URL)
                .param("nickname","test")
                .param("email","testEmail@email.com")
                .param("password","testPassword")
                .param("confirmPassword","testPassword")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm","email","invalid.email"))
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 실패 - 중복 닉네임 존재")
    @Test
    public void signUpWithDuplicatedNickname() throws Exception{
        mockMvc.perform(post(UrlConfig.SIGN_UP_URL)
                .param("nickname","testname")
                .param("email","test2@email.com")
                .param("password","testPassword")
                .param("confirmPassword","testPassword")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm","nickname","invalid.nickname"))
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 실패 - confirm 비밀번호 불일치")
    @Test
    public void notEqualConfirmPassword() throws Exception{
        mockMvc.perform(post(UrlConfig.SIGN_UP_URL)
                .param("nickname","test")
                .param("email","test3@email.com")
                .param("password","testPassword")
                .param("confirmPassword","incorrectPassword")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm","password","invalid.password"))
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 성공")
    @Test
    public void successSignUp() throws Exception{
        mockMvc.perform(post(UrlConfig.SIGN_UP_URL)
                .param("nickname","user")
                .param("email","user@email.com")
                .param("password","password1234")
                .param("confirmPassword","password1234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(authenticated());

        assertThat(accountRepository.existsByEmail("user@email.com")).isTrue();
        assertThat(accountRepository.existsByNickname("user")).isTrue();
    }

    @DisplayName("로그인 테스트 - 정상 로그인")
    @Test
    public void loginWithCorrectValue() throws Exception{
        mockMvc.perform(post(UrlConfig.LOGIN_URL)
                    .param("username",TEST_ID)
                    .param("password",TEST_PASSWORD)
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UrlConfig.HOME_URL))
                .andExpect(authenticated().withUsername(TEST_NICKNAME));
    }

    @DisplayName("로그인 테스트 - 존재하지 않는 아이디")
    @Test
    public void loginWithInvalidId() throws Exception{
        mockMvc.perform(post(UrlConfig.LOGIN_URL)
                .param("username","INVALID_ID")
                .param("password",TEST_PASSWORD)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그인 테스트 - 잘못된 비밀번호")
    @Test
    public void loginWithInvalidPassword() throws Exception{
        mockMvc.perform(post(UrlConfig.LOGIN_URL)
                .param("username","INVALID_ID")
                .param("password","INVALID_PASSWORD")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }
}