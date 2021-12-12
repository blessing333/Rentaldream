package com.blessing.rentaldream.modules.account.controller;

import com.blessing.rentaldream.infra.config.ViewNameConfig;
import com.blessing.rentaldream.modules.account.CurrentUser;
import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.service.WishListService;
import com.blessing.rentaldream.modules.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.blessing.rentaldream.infra.config.UrlConfig.*;
import static com.blessing.rentaldream.infra.config.ViewNameConfig.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WishListController {
    private final WishListService wishListService;
    private static final String FAVORITE_ADD_COMPLETE_MESSAGE = "해당 게시글이 관심 목록에 추가되었습니다";
    private static final String FAVORITE_DELETE_COMPLETE_MESSAGE = "해당 게시글이 관심 목록에서 삭제되었습니다";

    @GetMapping(FAVORITE_URL)
    public String createWishListView(@CurrentUser Account account,Model model){
        List<Post> wishList = wishListService.loadWishList(account);
        model.addAttribute("wishList",wishList);
        return ACCOUNT_WISHLIST_VIEW;
    }

    @PostMapping(FAVORITE_URL)
    public String addWishList(@CurrentUser Account account, @RequestParam Long postId, RedirectAttributes redirectAttributes, Model model){
       wishListService.addFavoriteToAccount(account,postId);
       String postIdUrl = "/"+postId.toString();
       redirectAttributes.addFlashAttribute("message",FAVORITE_ADD_COMPLETE_MESSAGE);
       return REDIRECT_URL + POST_URL + postIdUrl;
    }

    @DeleteMapping(FAVORITE_URL)
    public String deleteWishList(@CurrentUser Account account, @RequestParam Long postId,
                                 RedirectAttributes redirectAttributes, Model model,
                                 HttpServletRequest request){
        wishListService.deleteFavoriteFromAccount(account,postId);
        String postIdUrl = "/"+postId.toString();
        redirectAttributes.addFlashAttribute("message",FAVORITE_DELETE_COMPLETE_MESSAGE);
        String referer = request.getHeader("referer");
        log.info("referer----------------" + referer);
        String redirectUrl = referer!= null ? REDIRECT_URL + referer : REDIRECT_URL + POST_URL + postIdUrl;
        return redirectUrl;
    }

}
