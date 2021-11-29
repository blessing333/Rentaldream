package com.blessing.rentaldream.modules.post;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import com.blessing.rentaldream.modules.util.FileUploader;
import com.blessing.rentaldream.modules.notification.NotificationService;
import com.blessing.rentaldream.modules.notification.NotificationType;
import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.domain.PostTag;
import com.blessing.rentaldream.modules.post.domain.PostZone;
import com.blessing.rentaldream.modules.post.event.PostCreatedEvent;
import com.blessing.rentaldream.modules.post.form.PostForm;
import com.blessing.rentaldream.modules.post.repository.PostRepository;
import com.blessing.rentaldream.modules.post.thumbnail.Thumbnail;
import com.blessing.rentaldream.modules.post.thumbnail.ThumbnailRepository;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.tag.TagForm;
import com.blessing.rentaldream.modules.tag.TagService;
import com.blessing.rentaldream.modules.zone.Zone;
import com.blessing.rentaldream.modules.zone.ZoneForm;
import com.blessing.rentaldream.modules.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final ZoneService zoneService;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FileUploader fileUploader;
    private final ThumbnailRepository thumbnailRepository;
    private static final int ITEM_COUNT_PER_PAGE = 20;

    public Long addNewPost(Account account, PostForm postForm) {
        Post newPost = Post.createNewPost(postForm,account);
        addPostTagToPost(postForm,newPost);
        addPostZoneToPost(postForm,newPost);
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
            String originalFileName = postForm.getThumbnail().getOriginalFilename();
            String savedFileName = originalFileName + "-" + date.format(new Date());
            String filePath = saveThumbnailFileToOuterStorage(postForm.getThumbnail(),savedFileName);
            newPost.modifyThumbnailPath(filePath);
            Thumbnail postThumbnail = Thumbnail.createNewThumbnail(originalFileName,savedFileName,filePath,newPost);
            thumbnailRepository.save(postThumbnail);
        }catch (Exception e){
            e.printStackTrace();
            log.error("파일 입력 에러");
        }
        postRepository.save(newPost);
        sendWebNotification(newPost);
        applicationEventPublisher.publishEvent(new PostCreatedEvent(newPost));
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
        post.getTags().clear();
        post.getZones().clear();
        post.modifyPost(postForm);
        addPostTagToPost(postForm,post);
        addPostZoneToPost(postForm,post);
        if(!postForm.getThumbnail().isEmpty()) {
            try {
                Thumbnail thumbnail = thumbnailRepository.findByPost(post);
                fileUploader.deleteFile(thumbnail.getSavedFileName());
                thumbnailRepository.delete(thumbnail);
                SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
                String originalFileName = postForm.getThumbnail().getOriginalFilename();
                String savedFileName = originalFileName + "-" + date.format(new Date());
                String filePath = saveThumbnailFileToOuterStorage(postForm.getThumbnail(), savedFileName);
                post.modifyThumbnailPath(filePath);
                Thumbnail postThumbnail = Thumbnail.createNewThumbnail(originalFileName, savedFileName, filePath, post);
                thumbnailRepository.save(postThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("파일 입력 에러");
            }
        }
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

    @Transactional(readOnly = true)
    public List<Post> loadPostMatchingWithAccountTagAndZone(Account account,int maxItemCount){
        List<Tag> tagList = account.convertAccountTagsAsTagList();
        List<Zone> zoneList = account.convertAccountZonesAsZoneList();
        List<Post> result = postRepository.findPostWithTagListAndZoneList(tagList,zoneList);
        int itemCount = Math.min(result.size(), maxItemCount);
        return new ArrayList<>(result.subList(0, itemCount));
    }

    public List<Post> loadPostMatchingWithAccountZone(Account account, int maxItemCount) {
        List<Zone> zoneList = account.convertAccountZonesAsZoneList();
        List<Post> result = postRepository.findPostWithZoneList(zoneList);
        int itemCount = Math.min(result.size(), maxItemCount);
        return new ArrayList<>(result.subList(0, itemCount));
    }

    private void addPostTagToPost(PostForm postForm, Post newPost) {
        TagForm[] tagArray = postForm.getTagsAsArray();
        for (TagForm tagForm : tagArray) {
            Tag tag = tagService.addNewTag(tagForm.getTagName());
            PostTag newPostTag = PostTag.createNewPostTag(newPost, tag);
            newPost.addTag(newPostTag);
        }
    }

    private void addPostZoneToPost(PostForm postForm, Post newPost) {
        ZoneForm[] zoneArray = postForm.getZonesAsArray();
        for (ZoneForm zoneForm : zoneArray) {
            Zone zone = zoneService.findByCityAndProvince(zoneForm.getCity(),zoneForm.getProvince());
            PostZone postZone = PostZone.createNewPostZone(newPost, zone);
            newPost.addZone(postZone);
        }
    }

    private void sendWebNotification(Post post){
        List<Tag> tagList = post.getTagsAsTagList(post);
        List<Zone> zoneList = post.getZonesAsZoneList(post);
        List<Account> accounts = accountRepository.findAccountWithTagAndZone(tagList,zoneList);
        accounts.forEach(account -> {
            notificationService.createNotification(post, account, NotificationType.ITEM_UPDATED);
        });
    }

    private String saveThumbnailFileToOuterStorage(MultipartFile thumbnailFile,String fileName) throws Exception{
        return fileUploader.uploadFile(thumbnailFile,fileName);
    }
}
