package com.blessing.lentaldream.modules.comment;

import com.blessing.lentaldream.modules.post.repository.PostRepository;
import com.blessing.lentaldream.modules.post.repository.PostTagRepository;
import com.blessing.lentaldream.modules.post.repository.PostZoneRepository;
import com.blessing.lentaldream.modules.tag.TagRepository;
import com.blessing.lentaldream.modules.zone.ZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CommentControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ZoneRepository zoneRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired PostRepository postRepository;
    @Autowired PostTagRepository postTagRepository;
    @Autowired PostZoneRepository postZoneRepository;
    @Autowired TagRepository tagRepository;


}