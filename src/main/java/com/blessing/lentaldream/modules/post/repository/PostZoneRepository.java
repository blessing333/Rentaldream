package com.blessing.lentaldream.modules.post.repository;


import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.domain.PostZone;
import com.blessing.lentaldream.modules.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostZoneRepository extends JpaRepository<PostZone, Long> {
    PostZone findByPostAndZone(Post foundedPost, Zone zone);
}
