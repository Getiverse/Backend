package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import lombok.Data;

import java.util.List;
@Data
public class ArticlesPage {
    private Integer count;
    private List<Article> data;

    public ArticlesPage(Integer count, List<Article> data) {
        this.count = count;
        this.data = data;
    }
}
