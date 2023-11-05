package com.backend.getiverse.input.update;

import com.backend.getiverse.model.PostType;
import lombok.Data;

@Data
public class UpdateRatingInput {
    private int stars;
    private String comment;
    private String postId;
    private PostType postType;
    private int previousStars;
    private String ratingId;
}
