package com.blessing.rentaldream.modules.comment;
import com.blessing.rentaldream.modules.account.service.AccountService;
import com.blessing.rentaldream.modules.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
@RequiredArgsConstructor
public class CommentFormValidator implements Validator {
    private final AccountService accountService;
    private final PostService postService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CommentForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommentForm commentForm = (CommentForm) target;
        if(!accountService.checkValidAccountById(commentForm.getWriter())){
            errors.rejectValue("writer","존재하지 않는 사용자입니다.");
        }
        if(!postService.isExistPost(commentForm.getPost())){
            errors.rejectValue("post","존재하지 않는 게시글입니다");
        }

    }
}
