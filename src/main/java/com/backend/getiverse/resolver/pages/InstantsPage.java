package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Instant;
import lombok.Data;

import java.util.List;
@Data
public class InstantsPage {
    private Integer count;
    private List<Instant> data;

    public InstantsPage(Integer count, List<Instant> data) {
        this.count = count;
        this.data = data;
    }
}
