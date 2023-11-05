package com.backend.getiverse.input;

import com.backend.getiverse.model.support.CustomLink;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserInput {
    private String name;
    private String userName;
    private String profileImage;
    private String backgroundImage;
    private String bio;
    private List<String> socialLinks;
    private String contact;
    private List<CustomLink> links;
}
