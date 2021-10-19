package com.blessing.lentaldream.modules.comment;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.blessing.lentaldream.infra.config.UrlConfig.*;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentFormValidator commentFormValidator;
    @InitBinder("commentForm")
    public void initProfileFormBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(commentFormValidator);
    }

    @PostMapping(COMMENT_URL)
    public String createNewCommentAtPost(@Valid @ModelAttribute CommentForm commentForm, Errors error, Model model){
        commentService.saveComment(commentForm);
        return REDIRECT_URL + POST_URL + "/" +commentForm.getPost();
    }

    @DeleteMapping(COMMENT_URL +"/{id}")
    public String deleteComment(@CurrentUser Account account, @PathVariable Long id, RedirectAttributes redirectAttributes){
        Long postId = commentService.removeComment(account,id);
        String postUrl =  POST_URL +"/" + postId;
        redirectAttributes.addFlashAttribute("message","댓글이 삭제되었습니다");
        return REDIRECT_URL + postUrl;
    }

//    @PostMapping(COMMENT_EDIT_URL)
//    public String comme

}
