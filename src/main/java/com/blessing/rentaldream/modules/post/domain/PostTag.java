package com.blessing.rentaldream.modules.post.domain;

import com.blessing.rentaldream.modules.tag.Tag;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"post","tag"})
public class PostTag {
    @GeneratedValue @Id
    @Column(name = "POST_TAG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tag;

    static public PostTag createNewPostTag(Post post, Tag tag){
        PostTag instance = new PostTag();
        instance.setPost(post);
        instance.setTag(tag);
        return instance;
    }

}
