package com.blessing.rentaldream.modules.post.repository;

import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.domain.PostTag;
import com.blessing.rentaldream.modules.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    PostTag findByPostAndTag(Post foundedPost, Tag tag);
}
