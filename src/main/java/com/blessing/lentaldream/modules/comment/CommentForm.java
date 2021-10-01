package com.blessing.lentaldream.modules.comment;

import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Data
public class CommentForm {
    @NotNull
    private Long writer;
    @NotNull
    private Long post;
    @Lob
    private String content;
}
