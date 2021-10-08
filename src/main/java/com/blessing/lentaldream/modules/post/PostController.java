package com.blessing.lentaldream.modules.post;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.comment.CommentForm;
import com.blessing.lentaldream.modules.comment.CommentService;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.form.PostForm;
import com.blessing.lentaldream.modules.post.validator.PostFormValidator;
import com.blessing.lentaldream.modules.tag.TagService;
import com.blessing.lentaldream.modules.zone.ZoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static com.blessing.lentaldream.infra.config.UrlConfig.*;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.*;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final TagService tagService;
    private final ZoneService zoneService;
    private final PostService postService;
    private final PostFormValidator postFormValidator;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final CommentService commentService;

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
            model.addAttribute(postForm);
            return POST_NEW_POST_VIEW;
        }
        Long newPostId = postService.addNewPost(account,postForm);
        String postIdUrl = "/"+newPostId.toString();
        return REDIRECT_URL + POST_URL+postIdUrl;
    }

    @GetMapping(POST_URL+"/{id}")
    public String createPostView(@CurrentUser Account account, Model model, @PathVariable Long id){
        Post post = postService.loadPostInformationWithIncreaseViewCount(id);
        CommentForm commentForm = new CommentForm();
        commentForm.setPost(post.getId());
        model.addAttribute(post);
        if(account != null) {
            accountRepository.findById(account.getId()).ifPresent(model::addAttribute);
            commentForm.setWriter(account.getId());
        }

        model.addAttribute(commentForm);
        return POST_VIEW;
    }

    @GetMapping(POST_EDIT_URL+"/{id}")
    public String createPostModifyView(@CurrentUser Account account, Model model, @PathVariable Long id) throws JsonProcessingException {
        Post post = postService.loadPostInformationForModify(id,account);
        PostForm postForm = modelMapper.map(post,PostForm.class);
        model.addAttribute(postForm);
        String tagListJson = tagService.findAllTagAsJsonString();
        model.addAttribute("recommendTagList",tagListJson);
        String cityListJson = zoneService.findAllZonesAsJsonString();
        model.addAttribute("cityList",cityListJson);
        List<String> allTagNames = post.getAllTagName();
        model.addAttribute("allTagNames",allTagNames);
        List<String> allZoneNames = post.getAllZoneName();
        model.addAttribute("allZoneNames",allZoneNames);
        model.addAttribute("postId",post.getId());
        return POST_EDIT_VIEW;
    }

    @PostMapping(POST_EDIT_URL+"/{id}")
    public String modifyPost(@CurrentUser Account account, @Valid @ModelAttribute PostForm postForm, Errors errors, Model model, RedirectAttributes redirectAttributes,@PathVariable Long id){
        if(errors.hasErrors()){
            return POST_EDIT_VIEW;
        }
        Long postId = postService.modifyPost(postForm,id);
        String postIdUrl = "/"+postId.toString();
        return REDIRECT_URL + POST_URL+postIdUrl;
    }

    @DeleteMapping(POST_URL+"/{id}")
    public String deletePost(@CurrentUser Account account, RedirectAttributes redirectAttributes, @PathVariable Long id){
        postService.deletePost(id,account);
        return REDIRECT_URL + HOME_URL;
    }
}
