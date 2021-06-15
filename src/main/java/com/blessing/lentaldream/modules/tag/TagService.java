package com.blessing.lentaldream.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findAllTag() {
        return tagRepository.findAll();
    }

    @Transactional
    public Tag addNewTag(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if(tag == null){
            tag = Tag.createNewTag(tagName);
            tagRepository.save(tag);
        }
        return tag;
    }

    public Tag findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }
}
