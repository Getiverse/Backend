package com.backend.getiverse.input;

import lombok.Data;

@Data
public class ContactUsInput {
    private String name;
    private String email;
    private String message;
}
