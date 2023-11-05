package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoriesPage {
    private Integer count;
    private List<Category> data;

    public CategoriesPage(Integer count, List<Category> data) {
        this.count = count;
        this.data = data;
    }
}
