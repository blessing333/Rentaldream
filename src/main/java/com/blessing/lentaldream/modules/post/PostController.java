package com.blessing.lentaldream.modules.post;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.form.PostForm;
import com.blessing.lentaldream.modules.post.validator.PostFormValidator;
import com.blessing.lentaldream.modules.tag.TagService;
import com.blessing.lentaldream.modules.zone.ZoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.blessing.lentaldream.infra.config.UrlConfig.POST_NEW_POST_URL;
import static com.blessing.lentaldream.infra.config.UrlConfig.REDIRECT_URL;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.POST_NEW_POST_VIEW;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final TagService tagService;
    private final ZoneService zoneService;
    private final PostService postService;
    private final PostFormValidator postFormValidator;

    @InitBinder("postForm")
    public void init(WebDataBinder webDataBinder){
        webDataBinder.addValidators(postFormValidator);
    }

    @GetMapping(POST_NEW_POST_URL)
    public String createPostFormView(Model model) throws JsonProcessingException {
        PostForm postForm = new PostForm();
        model.addAttribute(postForm);
        String tagListJson = tagService.findAllTagAsJsonString();
        model.addAttribute("recommendTagList",tagListJson);
        String cityListJson = zoneService.findAllZonesAsJsonString();
        model.addAttribute("cityList",cityListJson);
        return POST_NEW_POST_VIEW;
    }

    @PostMapping(POST_NEW_POST_URL)
    public String createNewPost(@CurrentUser Account account, @Valid @ModelAttribute PostForm postForm, Errors errors, Model model, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            return POST_NEW_POST_VIEW;
        }
        postService.addNewPost(account,postForm);
        return REDIRECT_URL + POST_NEW_POST_URL;
    }
}
