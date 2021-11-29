package com.blessing.rentaldream.modules.post.form;

import com.blessing.rentaldream.modules.tag.TagForm;
import com.blessing.rentaldream.modules.zone.ZoneForm;
import com.google.gson.Gson;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostForm {
    @NotBlank
    @Length(min = 1,max = 20)
    private String title;

    private MultipartFile thumbnail;

    private String thumbnailPath;

    @NotBlank
    private String description;

    private Integer price;

    private String tagsWithJsonString;
    private String zonesWithJsonString;

    public TagForm[] getTagsAsArray(){
        Gson gson = new Gson();
        return gson.fromJson(tagsWithJsonString,TagForm[].class);
    }

    public ZoneForm[] getZonesAsArray(){
        Gson gson = new Gson();
        return gson.fromJson(zonesWithJsonString, ZoneForm[].class);
    }
}
