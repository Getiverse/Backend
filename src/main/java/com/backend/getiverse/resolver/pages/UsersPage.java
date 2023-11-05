package com.backend.getiverse.resolver.pages;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.User;
import lombok.Data;

import java.util.List;
@Data
public class UsersPage {
    private Integer count;
    private List<User> data;

    public UsersPage(Integer count, List<User> data) {
        this.count = count;
        this.data = data;
    }
}
