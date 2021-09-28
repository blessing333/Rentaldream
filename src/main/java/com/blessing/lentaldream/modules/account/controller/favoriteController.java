package com.blessing.lentaldream.modules.account.controller;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.blessing.lentaldream.infra.config.UrlConfig.*;

@Controller
@RequiredArgsConstructor
public class favoriteController {
    private final AccountService accountService;
    private static final String FAVORITE_ADD_COMPLETE_MESSAGE = "해당 게시글이 관심 목록에 추가되었습니다";
    private static final String FAVORITE_DELETE_COMPLETE_MESSAGE = "해당 게시글이 관심 목록에서 삭제되었습니다";

    @PostMapping(FAVORITE_URL)
    public String addFavorite(@CurrentUser Account account, @RequestParam Long postId, RedirectAttributes redirectAttributes, Model model){
       accountService.addFavoriteToAccount(account,postId);
       String postIdUrl = "/"+postId.toString();
       redirectAttributes.addFlashAttribute("message",FAVORITE_ADD_COMPLETE_MESSAGE);
       return REDIRECT_URL + POST_URL + postIdUrl;
    }

    @DeleteMapping(FAVORITE_URL)
    public String deleteFavorite(@CurrentUser Account account, @RequestParam Long postId, RedirectAttributes redirectAttributes,Model model){
        accountService.deleteFavoriteFromAccount(account,postId);
        String postIdUrl = "/"+postId.toString();
        redirectAttributes.addFlashAttribute("message",FAVORITE_DELETE_COMPLETE_MESSAGE);
        return REDIRECT_URL + POST_URL + postIdUrl;
    }

}
