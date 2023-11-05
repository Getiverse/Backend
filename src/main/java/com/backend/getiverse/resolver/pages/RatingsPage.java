package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Rating;
import lombok.Data;

import java.util.List;

@Data
public class RatingsPage {
    private Integer count;
    private List<Rating> data;

    public RatingsPage(Integer count, List<Rating> data) {
        this.count = count;
        this.data = data;
    }
}
