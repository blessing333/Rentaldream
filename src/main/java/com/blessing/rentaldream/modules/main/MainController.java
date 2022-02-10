package com.blessing.rentaldream.modules.main;
import com.blessing.rentaldream.modules.account.CurrentUser;
import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import com.blessing.rentaldream.modules.post.PostService;
import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.stream.Collectors;

import static com.blessing.rentaldream.infra.config.UrlConfig.HOME_URL;
import static com.blessing.rentaldream.infra.config.UrlConfig.LOGIN_URL;
import static com.blessing.rentaldream.infra.config.ViewNameConfig.LOGIN_VIEW;

@RequiredArgsConstructor
@Controller
public class MainController {
    private static final int MAX_ITEM_COUNT = 20;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    @GetMapping(HOME_URL)
    public String createMainView(@CurrentUser Account account, Model model){
        PageRequest pageRequest = PageRequest.of(0, MAX_ITEM_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> page = postRepository.findAll(pageRequest);
        List<Post> postList = page.getContent();
        model.addAttribute("postList",postList);
        if(account != null){
            account = accountRepository.findByNickname(account.getNickname());
            model.addAttribute("account",account);
            List<Post> matchedPostsWithZoneAndTag =  postService.loadPostMatchingWithAccountTagAndZone(account,MAX_ITEM_COUNT); // 관심지역 && 관심 상품 일치하는거 가져오기
            model.addAttribute("matchedPostsWithZoneAndTag",matchedPostsWithZoneAndTag);
            List<Post> matchedPostsWithZone =  postService.loadPostMatchingWithAccountZone(account,MAX_ITEM_COUNT);
            model.addAttribute("matchedPostsWithZone",matchedPostsWithZone);
            return "after-login-index";
        }
        return "index";
    }

    @GetMapping(LOGIN_URL)
    public String createLoginView() {
        return LOGIN_VIEW;
    }

    @GetMapping("/search")
    public String searchView(String keyword, Integer part,@PageableDefault(size = 10,
            sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<Post> postPage = postRepository.findByKeyword(keyword, pageable);
        int totalPage = postPage.getTotalPages();
        int pageCount = Math.min(totalPage, 10);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("keyword",keyword);
        model.addAttribute("part",part);
        model.addAttribute("postPage",postPage);
        return "search";
    }

}
