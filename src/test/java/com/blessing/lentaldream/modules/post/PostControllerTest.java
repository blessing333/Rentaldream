package com.blessing.lentaldream.modules.post;

import com.blessing.lentaldream.modules.account.LoginWith;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.post.form.PostForm;
import com.blessing.lentaldream.modules.post.repository.PostRepository;
import com.blessing.lentaldream.modules.post.repository.PostTagRepository;
import com.blessing.lentaldream.modules.post.repository.PostZoneRepository;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagRepository;
import com.blessing.lentaldream.modules.zone.Zone;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.blessing.lentaldream.modules.zone.ZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.blessing.lentaldream.infra.config.ErrorCodeConfig.MAX_OUT_TAG_COUNT;
import static com.blessing.lentaldream.infra.config.UrlConfig.POST_NEW_POST_URL;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.POST_NEW_POST_VIEW;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class PostControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ZoneRepository zoneRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired PostRepository postRepository;
    @Autowired PostTagRepository postTagRepository;
    @Autowired PostZoneRepository postZoneRepository;
    @Autowired TagRepository tagRepository;

    private static final String TEST_USER = "testUser";
    private static final String POST_FORM_NAME = "postForm";
    private Zone testZone = Zone.builder().city("test").localCityName("테스트시").province("테스트주").build();

    @DisplayName("게시글 작성 화면 표시")
    @Test
    @LoginWith(TEST_USER)
    public void createPostFormView() throws Exception {
        mockMvc.perform(get(POST_NEW_POST_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(POST_FORM_NAME))
                .andExpect(view().name(POST_NEW_POST_VIEW));
    }

    @DisplayName("게시글 작성 폼 전송 - 정상 처리")
    @Test
    @LoginWith(TEST_USER)
    public void createNewPostWithCollectValue() throws Exception {
        PostForm postForm = new PostForm();
        TagForm [] tagArr = new TagForm[2];
        tagArr[0] = new TagForm("테스트태그1");
        tagArr[1] = new TagForm("테스트태그2");
        String tagJsonString = objectMapper.writeValueAsString(tagArr);

        ZoneForm[] zoneArr = new ZoneForm[1];
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        zoneArr[0] = zoneForm;
        zoneRepository.save(testZone);
        String zoneJsonString = objectMapper.writeValueAsString(zoneArr);

        postForm.setDescription("상품설명");
        postForm.setThumbnail("이미지");
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);

        mockMvc.perform(post(POST_NEW_POST_URL)
            .param("title",postForm.getTitle())
            .param("thumbnail",postForm.getThumbnail())
            .param("price","120000")
            .param("description",postForm.getDescription())
            .param("tagsWithJsonString",tagJsonString)
            .param("zonesWithJsonString",zoneJsonString)
            .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(POST_NEW_POST_URL))
                .andExpect(model().hasNoErrors());

        Post foundedPost = postRepository.findByDescription(postForm.getDescription());
        assertNotNull(foundedPost);
        assertEquals(postForm.getTitle(),foundedPost.getTitle());
        assertEquals(postForm.getThumbnail(),foundedPost.getThumbnail());
        assertEquals(postForm.getPrice(),foundedPost.getPrice());
        assertEquals(postForm.getDescription(),foundedPost.getDescription());

        for (TagForm tagForm : tagArr) {
            Tag tag = tagRepository.findByTagName(tagForm.getTagName());
            assertNotNull(postTagRepository.findByPostAndTag(foundedPost,tag));
        }

        for (ZoneForm zoneForm2 : zoneArr) {
            Zone zone = zoneRepository.findByCityAndProvince(zoneForm2.getCity(),zoneForm2.getProvince());
            assertNotNull(postZoneRepository.findByPostAndZone(foundedPost,zone));
        }
    }

    @DisplayName("게시글 작성 폼 전송 실패 - 필수 값 누락")
    @Test
    @LoginWith(TEST_USER)
    public void createNewPostWithBlankValue() throws Exception {
        PostForm postForm = new PostForm();
        TagForm [] tagArr = new TagForm[2];
        tagArr[0] = new TagForm("테스트태그1");
        tagArr[1] = new TagForm("테스트태그2");
        String tagJsonString = objectMapper.writeValueAsString(tagArr);

        ZoneForm[] zoneArr = new ZoneForm[1];
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        zoneArr[0] = zoneForm;
        zoneRepository.save(testZone);
        String zoneJsonString = objectMapper.writeValueAsString(zoneArr);

        postForm.setDescription("상품설명");
        postForm.setThumbnail("이미지");
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);

        mockMvc.perform(post(POST_NEW_POST_URL)
                .param("tagsWithJsonString",tagJsonString)
                .param("zonesWithJsonString",zoneJsonString)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"title","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"thumbnail","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"description","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"price","NotNull"));
        Post foundedPost = postRepository.findByDescription(postForm.getDescription());
        assertNull(foundedPost);
    }
    @DisplayName("게시글 작성 폼 전송 실패 - 최대 태그 개수 초과")
    @Test
    @LoginWith(TEST_USER)
    public void createNewPostWithMaxOutTag() throws Exception {
        PostForm postForm = new PostForm();
        TagForm [] tagArr = new TagForm[5];
        tagArr[0] = new TagForm("테스트태그1");
        tagArr[1] = new TagForm("테스트태그2");
        tagArr[2] = new TagForm("테스트태그3");
        tagArr[3] = new TagForm("테스트태그4");
        tagArr[4] = new TagForm("테스트태그5");
        String tagJsonString = objectMapper.writeValueAsString(tagArr);

        ZoneForm[] zoneArr = new ZoneForm[1];
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        zoneArr[0] = zoneForm;
        zoneRepository.save(testZone);
        String zoneJsonString = objectMapper.writeValueAsString(zoneArr);

        postForm.setDescription("상품설명");
        postForm.setThumbnail("이미지");
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);

        mockMvc.perform(post(POST_NEW_POST_URL)
                .param("title",postForm.getTitle())
                .param("thumbnail",postForm.getThumbnail())
                .param("price","120000")
                .param("description",postForm.getDescription())
                .param("tagsWithJsonString",tagJsonString)
                .param("zonesWithJsonString",zoneJsonString)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"tagsWithJsonString", MAX_OUT_TAG_COUNT));

        Post foundedPost = postRepository.findByDescription(postForm.getDescription());
        assertNull(foundedPost);
    }
}