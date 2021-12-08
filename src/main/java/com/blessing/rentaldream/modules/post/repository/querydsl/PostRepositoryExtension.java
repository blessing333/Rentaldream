package com.blessing.rentaldream.modules.post.repository.querydsl;

import com.blessing.rentaldream.modules.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PostRepositoryExtension {
    Page<Post> findByKeyword(String keyword, Pageable pageable);


}
