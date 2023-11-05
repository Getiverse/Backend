package com.backend.getiverse.input;

import com.backend.getiverse.model.PostType;
import lombok.Data;

@Data
public class CreateReportInput {
    private String type;
    private PostType postType;
    private String postId;
}
