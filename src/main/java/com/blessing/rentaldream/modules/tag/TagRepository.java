package com.blessing.rentaldream.modules.tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag findByTagName(String tagName);
}
