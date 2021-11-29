package com.blessing.rentaldream.modules.post.thumbnail;

import com.blessing.rentaldream.modules.post.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thumbnail {
    @Id
    @GeneratedValue
    private Long id;

    private String originalFileName;
    private String savedFileName;
    private String filePath;
    private LocalDateTime createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    public static Thumbnail createNewThumbnail(String originalFileName, String savedFileName,String filePath,Post post){
        Thumbnail instance = new Thumbnail();
        instance.setOriginalFileName(originalFileName);
        instance.setSavedFileName(savedFileName);
        instance.setFilePath(filePath);
        instance.setCreatedDate(LocalDateTime.now());
        instance.setPost(post);
        return instance;
    }
}
