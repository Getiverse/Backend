package com.backend.getiverse.input.update;

import lombok.Data;

import java.util.List;

@Data
public class UpdateArticleInput {
    private String image;
    private String title;
    private List<String> categories;
    private String content;
    private int readTime;
    private String id;
}
