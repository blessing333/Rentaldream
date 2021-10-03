package com.blessing.lentaldream.modules.post.repository;

import com.blessing.lentaldream.modules.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByDescription(String description); //only test

}
