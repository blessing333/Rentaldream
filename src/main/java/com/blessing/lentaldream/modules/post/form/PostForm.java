package com.blessing.lentaldream.modules.post.form;

import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.google.gson.Gson;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostForm {
    @NotBlank
    @Length(min = 1,max = 20)
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    private String description;

    @NotNull
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
