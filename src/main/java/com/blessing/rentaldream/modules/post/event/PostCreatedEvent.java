package com.blessing.rentaldream.modules.post.event;

import com.blessing.rentaldream.modules.post.domain.Post;
import lombok.Getter;
@Getter
public class PostCreatedEvent{
    private Post post;
    public PostCreatedEvent(Post post) {
        this.post = post;
    }
}
