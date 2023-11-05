package com.backend.getiverse.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "library")
public class Library {
    @Id
    private String id;
    private String userId;
    private Boolean isPrivate;
    private List<String> instants;
    private List<String> articles;
    private String image;
    private String description;
    private LocalDate createdAt;
    private String title;

    public Library(String userId, Boolean isPrivate, String image,
                   String title, String description) {
        this.userId = userId;
        this.isPrivate = isPrivate;
        this.image = image;
        this.title = title;
        this.instants = new ArrayList<>();
        this.articles = new ArrayList<>();
        this.createdAt = LocalDate.now();
        this.description = description;
    }
}
