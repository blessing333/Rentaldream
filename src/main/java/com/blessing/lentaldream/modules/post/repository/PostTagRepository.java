package com.blessing.lentaldream.modules.post.repository;

import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.domain.PostTag;
import com.blessing.lentaldream.modules.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    PostTag findByPostAndTag(Post foundedPost, Tag tag);
}
