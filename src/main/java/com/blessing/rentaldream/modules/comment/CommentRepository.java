package com.blessing.rentaldream.modules.comment;

import com.blessing.rentaldream.modules.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findByPost(Post post);
}
