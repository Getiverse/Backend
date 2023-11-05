package com.backend.getiverse.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "posts")
public class Article {
    @Id
    private String id;
    private String userId;
    private String image;
    @TextIndexed(weight = 2)
    private String title;
    private LocalDate createdAt;
    @TextIndexed
    private String content;
    private Integer ratingsNumber;
    private int ratingSum;
    private int views;
    private List<String> categories;
    private LocalDate postDate;
    private LocalTime postTime;
    private List<Reports> reports;
    private boolean isVisible;
    private int readTime;
    @TextScore
    private Float score;


    public Article(String userId, String image, String title,
                   String content, List<String> categories, LocalDate postDate, LocalTime postTime, int readTime) {
        this.readTime = readTime;
        this.userId = userId;
        this.title = title;
        this.createdAt = LocalDate.now();
        this.content = content;
        this.ratingsNumber = 0;
        this.ratingSum = 0;
        this.views = 0;
        this.image = image;
        this.categories = categories;
        this.postDate = postDate;
        this.postTime = postTime;
        reports = new ArrayList<>();
        isVisible = true;
    }
}