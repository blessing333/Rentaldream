package com.blessing.rentaldream.modules.post.repository;

import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByDescription(String description); //only test

    @Query("SELECT distinct pt.post from PostTag pt join PostZone pz on pz.post = pt.post where pt.tag in :tag and pz.zone in :zone order by pt.post.createdDate desc")
    List<Post> findPostWithTagListAndZoneList(@Param("tag") List<Tag> tag, @Param("zone") List<Zone> zone);

    @Query("SELECT distinct pz.post from PostZone pz where pz.zone in :zone order by pz.post.createdDate desc")
    List<Post> findPostWithZoneList(@Param("zone") List<Zone> zoneList);
}
