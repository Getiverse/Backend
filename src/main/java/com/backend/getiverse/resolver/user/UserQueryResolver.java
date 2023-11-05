package com.backend.getiverse.resolver.user;

import com.backend.getiverse.Service.query.ScoreCalculatorService;
import com.backend.getiverse.Service.query.SuggestAuthorsService;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.*;
import com.backend.getiverse.repository.*;
import com.backend.getiverse.resolver.pages.UsersPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class UserQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private RatingRepository ratingRepo;
    @Autowired
    private InstantRepository instantRepository;
    @Autowired
    private SuggestAuthorsService suggestAuthorsService;
    @Autowired
    private ScoreCalculatorService scoreCalculatorService;

    public User findUserById(String id) {
        Optional<User> user = userRepo.findById(id);
        if(!user.isEmpty()) {
            user.get().setViews(user.get().getViews() + 1);
            if(user.get().getViews() % 1000 == 0) {
                user.get().setScore(scoreCalculatorService.calculateUserScore(user.get()));
                userRepo.save(user.get());
            }
        }
        return user.get();
    }

    public UsersPage findAllFollowByUserId(String userId, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        List<String> followsId = userRepo.findById(userId).get().getFollow();
        org.springframework.data.domain.Page<User> userPage = userRepo.getUsersByUserIds(followsId, pageable);
        return new UsersPage((int) userPage.getTotalElements(), userPage.getContent());
    }

    public UsersPage getUserFollowers(String id, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<User> userPage = userRepo.getUsersByUserIds(userRepo.findById(id).get().getFollowers(), pageable);
        return new UsersPage((int) userPage.getTotalElements(), userPage.getContent());
    }

    public UsersPage getUserFollowing(String id, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<User> userPage = userRepo.getUsersByUserIds(userRepo.findById(id).get().getFollow(), pageable);
        return new UsersPage((int) userPage.getTotalElements(), userPage.getContent());
    }

    public List<Rating> getUserPostsRating(String id) {
        //$in operation works only with array of strings
        return ratingRepo.findByUserId(id);
    }

    public UsersPage findAllUsers(Page page) {
        SecurityService sr = new SecurityService();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<User> userPage = suggestAuthorsService.suggest(pageable);
        return new UsersPage((int) userPage.getTotalElements(), userPage.getContent());
    }

    public int getUserPostViews(String id) {
        return 0;
    }

    public List<User> findUsersByIds(List<String> ids) {
        if (ids.size() > 1) {
            Pageable pageable = PageRequest.of(0, ids.size());
            return userRepo.getUsersByUserIds(ids, pageable).getContent();
        }
        ArrayList<User> user = new ArrayList<>();
        user.add(userRepo.findById(ids.get(0)).get());
        return user;
    }
}
