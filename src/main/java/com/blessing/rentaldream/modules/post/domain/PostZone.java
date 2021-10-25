package com.blessing.rentaldream.modules.post.domain;

import com.blessing.rentaldream.modules.zone.Zone;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class PostZone {
    @Id @GeneratedValue
    @Column(name = "POST_ZONE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    static public PostZone createNewPostZone(Post post, Zone zone){
        PostZone instance = new PostZone();
        instance.setPost(post);
        instance.setZone(zone);
        return instance;
    }
}
