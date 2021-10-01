package com.blessing.lentaldream.modules.post;

import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.domain.PostTag;
import com.blessing.lentaldream.modules.post.domain.PostZone;
import com.blessing.lentaldream.modules.post.form.PostForm;
import com.blessing.lentaldream.modules.post.repository.PostRepository;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagService;
import com.blessing.lentaldream.modules.zone.Zone;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.blessing.lentaldream.modules.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final ZoneService zoneService;

    public Long addNewPost(Account account, PostForm postForm) {
        Post newPost = Post.createNewPost(postForm.getTitle(),account,postForm.getDescription(),postForm.getThumbnail(),postForm.getPrice());
        TagForm[] tagArray = postForm.getTagsAsArray();
        for (TagForm tagForm : tagArray) {
            Tag tag = tagService.addNewTag(tagForm.getTagName());
            PostTag newPostTag = PostTag.createNewPostTag(newPost, tag);
            newPost.addTag(newPostTag);
        }
        ZoneForm[] zoneArray = postForm.getZonesAsArray();
        for (ZoneForm zoneForm : zoneArray) {
            Zone zone = zoneService.findByCityAndProvince(zoneForm.getCity(),zoneForm.getProvince());
            PostZone postZone = PostZone.createNewPostZone(newPost, zone);
            newPost.addZone(postZone);
        }
        postRepository.save(newPost);
        return newPost.getId();
    }

    public Post loadPostInformationWithIncreaseViewCount(Long id) {
        Post foundPost = loadPostInformation(id);
        foundPost.increaseViewCount();
        return foundPost;
    }

    public Post loadPostInformation(Long id) {
        Optional<Post> foundPost = postRepository.findById(id);
        foundPost.orElseThrow(IllegalArgumentException::new);
        return foundPost.get();
    }

    public Post loadPostInformationForModify(Long id, Account account) {
        Post post = loadPostInformation(id);
        if(!post.isPoster(account)){
            throw new AccessDeniedException("해당 게시글을 수정할 권한이 없습니다");
        }
        return post;
    }

    public Long modifyPost(PostForm postForm, Long postId) {
        Post post = loadPostInformation(postId);
        post.modifyPost(postForm);
        post.getTags().clear();
        TagForm[] tagArray = postForm.getTagsAsArray();
        for (TagForm tagForm : tagArray) {
            Tag tag = tagService.addNewTag(tagForm.getTagName());
            PostTag newPostTag = PostTag.createNewPostTag(post, tag);
            post.addTag(newPostTag);
        }
        post.getZones().clear();
        ZoneForm[] zoneArray = postForm.getZonesAsArray();
        for (ZoneForm zoneForm : zoneArray) {
            Zone zone = zoneService.findByCityAndProvince(zoneForm.getCity(),zoneForm.getProvince());
            PostZone postZone = PostZone.createNewPostZone(post, zone);
            post.addZone(postZone);
        }
        postRepository.save(post);
        return post.getId();
    }

    public void deletePost(Long id, Account account) {
        Post post = loadPostInformation(id);
        if(!post.isPoster(account)) {
            throw new AccessDeniedException("해당 게시글의 작성자가 아닙니다;");
        }
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public boolean isExistPost(Long postId){
        return postRepository.existsById(postId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId);
    }
}
