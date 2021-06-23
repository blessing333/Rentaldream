package com.blessing.lentaldream.modules.post;

import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.domain.PostTag;
import com.blessing.lentaldream.modules.post.domain.PostZone;
import com.blessing.lentaldream.modules.post.form.PostForm;
import com.blessing.lentaldream.modules.post.repository.PostRepository;
import com.blessing.lentaldream.modules.post.repository.PostTagRepository;
import com.blessing.lentaldream.modules.post.repository.PostZoneRepository;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagService;
import com.blessing.lentaldream.modules.zone.Zone;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.blessing.lentaldream.modules.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostZoneRepository postZoneRepository;
    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;
    private final TagService tagService;
    private final ZoneService zoneService;

    public void addNewPost(Account account, PostForm postForm) {
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
            PostZone postZone = PostZone.createNewPostZone(newPost,zone);
            newPost.addZone(postZone);
        }

        postRepository.save(newPost);
    }
}
