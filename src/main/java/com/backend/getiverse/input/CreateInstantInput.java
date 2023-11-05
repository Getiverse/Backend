package com.backend.getiverse.input;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateInstantInput {
    private String title;
    private String content;
    private String image;
    List<String> categories;
    private String postDate;
    private String postTime;
}

