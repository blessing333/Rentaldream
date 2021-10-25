package com.blessing.rentaldream.modules.post.repository;


import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.domain.PostZone;
import com.blessing.rentaldream.modules.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostZoneRepository extends JpaRepository<PostZone, Long> {
    PostZone findByPostAndZone(Post foundedPost, Zone zone);
}
