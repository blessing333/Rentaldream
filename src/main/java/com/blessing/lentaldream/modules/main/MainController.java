package com.blessing.lentaldream.modules.main;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.blessing.lentaldream.infra.config.UrlConfig.HOME_URL;
import static com.blessing.lentaldream.infra.config.UrlConfig.LOGIN_URL;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.LOGIN_VIEW;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final PostRepository postRepository;
    private static final int MAX_ITEM_COUNT = 20;
    @GetMapping(HOME_URL)
    public String createMainView(@CurrentUser Account account, Model model){
        PageRequest pageRequest = PageRequest.of(0, MAX_ITEM_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> page = postRepository.findAll(pageRequest);
        List<Post> postList = page.getContent();
        model.addAttribute("postList",postList);
        return "index";
    }

    @GetMapping(LOGIN_URL)
    public String createLoginView() {
        return LOGIN_VIEW;
    }

}
