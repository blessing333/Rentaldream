package com.blessing.lentaldream.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public Tag addNewTag(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if(tag == null){
            tag = Tag.createNewTag(tagName);
            tagRepository.save(tag);
        }
        return tag;
    }

}
