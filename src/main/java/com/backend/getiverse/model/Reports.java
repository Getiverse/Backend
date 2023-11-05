package com.backend.getiverse.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "report")
public class Reports {
    @Id
    private String id;
    private String userId;
    private String type;
    private PostType postType;
    private String postId;

    public Reports(String userId, String type, PostType postType,String postId) {
        this.userId = userId;
        this.postType = postType;
        this.postId = postId;
        this.type = type;
    }
}
