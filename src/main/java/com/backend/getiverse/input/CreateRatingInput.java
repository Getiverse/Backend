package com.backend.getiverse.input;

import com.backend.getiverse.model.PostType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateRatingInput {
    private int stars;
    private String comment;
    private String postId;
    private PostType postType;
}
