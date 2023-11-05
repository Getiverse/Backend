package com.backend.getiverse.controller.support;

import lombok.Data;

import java.util.List;

@Data
public class SearchPage {
    private List<?> content;
    private int numberOfElements;

    public SearchPage(List<?> content, int numberOfElements) {
        this.content = content;
        this.numberOfElements = numberOfElements;
    }
}
