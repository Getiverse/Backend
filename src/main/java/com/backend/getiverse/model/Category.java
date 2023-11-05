package com.backend.getiverse.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private String id;
    private String name;
    private String image;

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
