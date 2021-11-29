package com.blessing.rentaldream.modules.post.thumbnail;

import com.blessing.rentaldream.modules.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail,Long> {
    Thumbnail findByPost(Post post);
}
