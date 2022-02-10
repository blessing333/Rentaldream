package com.blessing.rentaldream.modules.post;

import com.blessing.rentaldream.modules.account.CurrentUser;
import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import com.blessing.rentaldream.modules.comment.CommentForm;
import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.form.PostForm;
import com.blessing.rentaldream.modules.post.repository.PostRepository;
import com.blessing.rentaldream.modules.post.validator.PostFormValidator;
import com.blessing.rentaldream.modules.tag.TagService;
import com.blessing.rentaldream.modules.zone.ZoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.blessing.rentaldream.infra.config.UrlConfig.*;
import static com.blessing.rentaldream.infra.config.ViewNameConfig.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final TagService tagService;
    private final ZoneService zoneService;
    private final PostService postService;
    private final PostFormValidator postFormValidator;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

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
        model.addAttribute("recommendPostList");
        model.addAttribute(commentForm);
        return POST_VIEW;
    }

    @GetMapping(POST_EDIT_URL+"/{id}")
    public String createPostModifyView(@CurrentUser Account account, Model model, @PathVariable Long id) throws JsonProcessingException {
        Post post = postService.loadPostInformationForModify(id,account);
        PostForm postForm = new PostForm();
        postForm.setDescription(post.getDescription());
        postForm.setPrice(post.getPrice());
        postForm.setThumbnailPath(post.getThumbnail());
        postForm.setTitle(post.getTitle());
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
            log.error(Objects.requireNonNull(errors.getFieldError()).toString());
            return POST_EDIT_VIEW;
        }
        Long postId = postService.modifyPost(postForm,id);
        String postIdUrl = "/"+postId.toString();
        return REDIRECT_URL + POST_URL+postIdUrl;
    }

    @DeleteMapping(POST_URL+"/{id}")
    public String deletePost(@CurrentUser Account account, @PathVariable Long id) throws Exception{
        postService.deletePost(id,account);
        return REDIRECT_URL + HOME_URL;
    }

    @GetMapping(POST_LIST_URL)
    public String createAllPostsView(Integer part,Model model,
                                     @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable){
        //현재 페이지 번호,
        Page<Post>  postPage = postRepository.findAll(pageable);
        int totalPage = postPage.getTotalPages();
        int pageCount = Math.min(totalPage, 10);
        System.out.println("pageCount ---------" + pageCount);
        System.out.println("part ---------" + part);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("part",part);
        model.addAttribute("postPage",postPage);
        return POST_LIST_VIEW;
    }
}
