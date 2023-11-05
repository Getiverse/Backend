package com.backend.getiverse.resolver.rating;

import com.backend.getiverse.model.PostType;
import com.backend.getiverse.model.Rating;
import com.backend.getiverse.repository.RatingRepository;
import com.backend.getiverse.resolver.pages.RatingsPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class RatingQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private RatingRepository ratingRepository;

    public RatingsPage findAllRatings(com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Rating> ratings = ratingRepository.findAllRatings(pageable);
        return new RatingsPage((int) ratings.getTotalElements(), ratings.getContent());
    }

    public RatingsPage findRatingsByPostId(String postId, PostType postType, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Rating> ratings =  ratingRepository.findByPostIdAndPostType(postId, postType, pageable);
        return new RatingsPage((int) ratings.getTotalElements(), ratings.getContent());

    }

    public Rating findRatingById(String id) {
        return ratingRepository.findByid(id);
    }

}