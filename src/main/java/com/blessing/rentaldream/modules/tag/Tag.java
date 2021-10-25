package com.blessing.rentaldream.modules.tag;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Tag {
    @GeneratedValue
    @Id
    @Column(name = "TAG_ID")
    private Long id;
    private String tagName;

    public static Tag createNewTag(String tagName) {
        Tag instance = new Tag();
        instance.setTagName(tagName);
        return instance;
    }
}
