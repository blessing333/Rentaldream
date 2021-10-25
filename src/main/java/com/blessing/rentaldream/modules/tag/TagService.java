package com.blessing.rentaldream.modules.tag;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

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

    public String findAllTagAsJsonString() throws JsonProcessingException {
        List<String>allTags = tagRepository.findAll().stream().map(Tag::getTagName).collect(Collectors.toList());
        return objectMapper.writeValueAsString(allTags);
    }


}
