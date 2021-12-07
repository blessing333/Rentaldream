package com.blessing.rentaldream.modules.post;

import com.blessing.rentaldream.infra.config.UrlConfig;
import com.blessing.rentaldream.infra.file.FileManager;
import com.blessing.rentaldream.modules.account.LoginWith;
import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.repository.AccountRepository;
import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.form.PostForm;
import com.blessing.rentaldream.modules.post.repository.PostRepository;
import com.blessing.rentaldream.modules.post.repository.PostTagRepository;
import com.blessing.rentaldream.modules.post.repository.PostZoneRepository;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.tag.TagForm;
import com.blessing.rentaldream.modules.tag.TagRepository;
import com.blessing.rentaldream.modules.zone.Zone;
import com.blessing.rentaldream.modules.zone.ZoneForm;
import com.blessing.rentaldream.modules.zone.ZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.nio.charset.StandardCharsets;

import static com.blessing.rentaldream.infra.config.ErrorCodeConfig.MAX_OUT_TAG_COUNT;
import static com.blessing.rentaldream.infra.config.UrlConfig.*;
import static com.blessing.rentaldream.infra.config.UrlConfig.POST_EDIT_URL;
import static com.blessing.rentaldream.infra.config.UrlConfig.POST_NEW_POST_URL;
import static com.blessing.rentaldream.infra.config.ViewNameConfig.POST_NEW_POST_VIEW;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class PostControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ZoneRepository zoneRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired PostTagRepository postTagRepository;
    @Autowired PostZoneRepository postZoneRepository;
    @Autowired TagRepository tagRepository;
    @Autowired FileManager fileManager;
    @Autowired AccountRepository accountRepository;

    private static final String TEST_USER = "testUser";
    private static final String POST_FORM_NAME = "postForm";
    private static final String TEST_THUMBNAIL_NAME = "test.png";
    private static final String TEST_THUMBNAIL_PATH = "src/test/resources/image/";
    private Zone testZone = Zone.builder().city("test").localCityName("테스트시").province("테스트주").build();
    private static final MockMultipartFile thumbnail = new MockMultipartFile(
            "thumbnail",
            TEST_THUMBNAIL_NAME,
            "image/png",
            "<<png data>>".getBytes());

    @AfterEach
    void deleteTestThumbnailFile(){
        try {
            fileManager.deleteFile(TEST_THUMBNAIL_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        postForm.setThumbnail(thumbnail);
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);

        mockMvc.perform(multipart(POST_NEW_POST_URL)
                .file(thumbnail)
            .param("title",postForm.getTitle())
            .param("price","120000")
            .param("description",postForm.getDescription())
            .param("tagsWithJsonString",tagJsonString)
            .param("zonesWithJsonString",zoneJsonString)
            .with(csrf()))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection());


        Post foundedPost = postRepository.findByDescription(postForm.getDescription());
        assertNotNull(foundedPost);
        assertEquals(postForm.getTitle(),foundedPost.getTitle());
        assertEquals(foundedPost.getThumbnail(),TEST_THUMBNAIL_PATH+TEST_THUMBNAIL_NAME);
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
        postForm.setThumbnail(thumbnail);
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);

        mockMvc.perform(multipart(POST_NEW_POST_URL)
                .param("tagsWithJsonString",tagJsonString)
                .param("zonesWithJsonString",zoneJsonString)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"title","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode(POST_FORM_NAME,"thumbnail","wrong.thumbnail"))
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
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);

        mockMvc.perform(multipart(POST_NEW_POST_URL)
                .file(thumbnail)
                .param("title",postForm.getTitle())
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
    /**
        게시글 수정,
        게시글 삭제,
    * */
    @Test
    @DisplayName("게시글 수정 - 정상 처리")
    @LoginWith(TEST_USER)
    public void editPostTest() throws Exception{
        Account account = accountRepository.findByNickname(TEST_USER);
        PostForm postForm = createPostForm();
        Long id = postService.addNewPost(account,postForm);
        Post post = postRepository.findById(id).get();

        PostForm postFixForm= new PostForm();
        String fixedTitle = "수정된 제목";
        Integer fixedPrice = 9999999;
        String fixedDescription = "수정된 설명";
        MockMultipartFile fixedThumbnail = new MockMultipartFile(
                "thumbnail",
                "fixedFile",
                "image/png",
                "<<png>>".getBytes()
        );

        postFixForm.setThumbnail(fixedThumbnail);
        postFixForm.setTagsWithJsonString(postForm.getTagsWithJsonString());
        postFixForm.setZonesWithJsonString(postForm.getZonesWithJsonString());
        postFixForm.setThumbnailPath(post.getThumbnail());
        postFixForm.setTitle(fixedTitle);
        postFixForm.setPrice(fixedPrice);
        postFixForm.setDescription(fixedDescription);

        mockMvc.perform(multipart(POST_EDIT_URL+"/"+id)
        .file(fixedThumbnail)
        .param("title",postFixForm.getTitle())
        .param("price",postFixForm.getPrice().toString())
        .param("description",postFixForm.getDescription())
        .param("tagsWithJsonString", postFixForm.getTagsWithJsonString())
        .param("zonesWithJsonString", postFixForm.getZonesWithJsonString())
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(model().attributeHasNoErrors());

        Post foundedPost = postRepository.findByDescription(postFixForm.getDescription());
        assertNotNull(foundedPost);
        assertEquals(postFixForm.getTitle(),foundedPost.getTitle());
        assertEquals(postFixForm.getPrice(),foundedPost.getPrice());
        assertEquals(postFixForm.getDescription(),foundedPost.getDescription());
    }

    @DisplayName("게시글 삭제")
    @Test
    @LoginWith(TEST_USER)
    public void deleteTest() throws Exception{
        Account account = accountRepository.findByNickname(TEST_USER);
        PostForm postForm = createPostForm();
        Long id = postService.addNewPost(account,postForm);
        mockMvc.perform(delete(POST_URL+"/"+id)
        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        Post foundPost = postRepository.findByDescription(postForm.getDescription());
        assertNull(foundPost);
    }

    private PostForm createPostForm() throws Exception{
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
        postForm.setThumbnail(thumbnail);
        postForm.setPrice(120000);
        postForm.setTitle("제목");
        postForm.setTagsWithJsonString(tagJsonString);
        postForm.setZonesWithJsonString(zoneJsonString);
        return postForm;
    }


}