package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Library;
import lombok.Data;

import java.util.List;
@Data
public class LibrariesPage {
    private Integer count;
    private List<Library> data;

    public LibrariesPage(Integer count, List<Library> data) {
        this.count = count;
        this.data = data;
    }
}
