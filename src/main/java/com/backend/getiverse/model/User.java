package com.backend.getiverse.model;


import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.support.CustomLink;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private List<String> selectedCategories;
    private String name;
    @TextIndexed
    private String userName;
    private String bio;
    private String profileImage;
    private LocalDate createdAt;
    private String backgroundImage;
    private List<String> followers;
    private List<String> follow;
    private Integer numberOfArticles;
    private Integer numberOfInstants;
    private List<String> saved;
    private List<String> socialLinks;
    private List<CustomLink> links;
    private String contact;
    private List<String> blockedUsers;
    private int views;
    @TextScore
    private Float score;

    public User(String name, String profileImage, String backgroundImage, String bio) {
        SecurityService sr = new SecurityService();
        if (sr.getUser() != null)
            this.id = sr.getUser().getUid();
        this.contact = "";
        this.name = name;
        this.bio = bio;
        this.backgroundImage = backgroundImage;
        this.profileImage = profileImage;
        this.followers = new ArrayList<>();
        this.follow = new ArrayList<>();
        this.saved = new ArrayList<>();
        this.selectedCategories = new ArrayList<>();
        this.socialLinks = new ArrayList<>();
        this.links = new ArrayList<>();
        this.numberOfArticles = 0;
        this.numberOfInstants = 0;
        this.views = 0;
        createdAt = LocalDate.now();
        this.blockedUsers = new ArrayList<>();
    }
}
