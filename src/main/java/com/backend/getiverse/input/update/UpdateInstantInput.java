package com.backend.getiverse.input.update;

import lombok.Data;

import java.util.List;
@Data
public class UpdateInstantInput {
    private String image;
    private String title;
    private List<String> categories;
    private String content;
    private String id;
}
