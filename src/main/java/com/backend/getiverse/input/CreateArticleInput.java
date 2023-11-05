package com.backend.getiverse.input;

import lombok.Data;

import java.util.List;

@Data
public class CreateArticleInput {
    private String image;
    private String title;
    private List<String> categories;
    private String content;
    private String postDate;
    private String postTime;
    private int readTime;
}