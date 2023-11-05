package com.backend.getiverse.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "ratings")
public class Rating {
    @Id
    private String id;
    private String userId;
    private int stars;
    private LocalDate createdAt;
    private String comment;
    private List<String> usefulRating;
    private List<String> notUsefulRating;
    private String postId;
    private PostType postType;

    public Rating(String userId, int stars, String comment, String postId, PostType postType) {
        this.userId = userId;
        this.stars = stars;
        this.comment = comment;
        this.postId = postId;
        this.postType = postType;
        createdAt = LocalDate.now();
        this.usefulRating = new ArrayList<>();
        this.notUsefulRating = new ArrayList<>();
    }
}
