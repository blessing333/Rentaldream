package com.blessing.rentaldream.modules.account.service;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.domain.WishList;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import com.blessing.rentaldream.modules.account.repository.WishListRepository;
import com.blessing.rentaldream.modules.post.PostService;
import com.blessing.rentaldream.modules.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListService {
    private final AccountRepository accountRepository;
    private final WishListRepository wishListRepository;
    private final PostService postService;

    public void addFavoriteToAccount(Account account, Long postId){
        Account foundAccount = accountRepository.findByNickname(account.getNickname());
        Post post = postService.loadPostInformation(postId);
        WishList wishList = WishList.createNewFavorite(account, post);
        if(!foundAccount.checkFavorite(post)) {
            post.increaseFavoriteCount();
            foundAccount.addFavorite(wishList);
        }
    }

    public void deleteFavoriteFromAccount(Account account, Long postId) {
        Account foundAccount = accountRepository.findByNickname(account.getNickname());
        Post post = postService.loadPostInformation(postId);
        WishList target = WishList.createNewFavorite(account,post);
        if(foundAccount.checkFavorite(post)) {
            foundAccount.deleteFavorite(target);
            post.decreaseFavoriteCount();
        }
    }

    public List<Post> loadWishList(Account account) {
        List<WishList> wishListList = wishListRepository.findAllByAccount(account);
        return wishListList.stream().map(WishList::getPost).collect(Collectors.toList());
    }
}
