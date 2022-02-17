package com.blessing.rentaldream.modules.post.repository.querydsl;

import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostRepositoryExtension {
    Page<Post> findByKeyword(String keyword, Pageable pageable);
    Page<Post> findPostsByZoneListWithPaging(List<Zone> zoneList, Pageable pageable);
    Page<Post> findPostsByZoneListAndTagListWithPaging(List<Tag> tagList, List<Zone> zoneList, Pageable pageable);
}
