package com.backend.getiverse.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReplyInput {
    private String repliedUser;
    private String ratingId;
    private String comment;
}
