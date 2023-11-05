package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Reply;
import lombok.Data;

import java.util.List;
@Data
public class RepliesPage {
    private Integer count;
    private List<Reply> data;

    public RepliesPage(Integer count, List<Reply> data) {
        this.count = count;
        this.data = data;
    }
}
