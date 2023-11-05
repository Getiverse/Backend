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
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "instants")
public class Instant {
    @Id
    private String id;
    private String userId;
    @TextIndexed(weight = 2)
    private String title;
    @TextIndexed
    private String content;
    private String image;
    private LocalDate createdAt;
    private LocalDate postDate;
    private LocalTime postTime;
    private Integer ratingsNumber;
    private int views;
    private int ratingSum;
    private List<String> categories;
    private List<Reports> reports;
    private boolean isVisible;
    @TextScore
    private Float score;

    public Instant(String userId, String title, String content,
           String image, List<String> categories, LocalDate postDate, LocalTime postTime) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.ratingsNumber = 0;
        this.views = 0;
        this.ratingSum = 0;
        this.categories = categories;
        this.createdAt = LocalDate.now();
        this.postDate = postDate;
        this.postTime = postTime;
        reports = new ArrayList<>();
        isVisible = true;
    }
}
