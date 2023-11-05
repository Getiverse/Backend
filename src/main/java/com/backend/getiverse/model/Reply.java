package com.backend.getiverse.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "replies")
public class Reply {
    @Id
    private String id;
    private String userId;
    private String repliedUser;
    private String ratingId;
    private String comment;
    private LocalDate createdAt;
    private List<String> usefulRating;
    private List<String> notUsefulRating;

    public Reply(String userId, String repliedUser, String ratingId, String comment) {
        this.userId = userId;
        this.repliedUser = repliedUser;
        this.ratingId = ratingId;
        this.comment = comment;
        this.createdAt = LocalDate.now();
        this.usefulRating = new ArrayList<>();
        this.notUsefulRating = new ArrayList<>();
    }
}
