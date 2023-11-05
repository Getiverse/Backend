package com.backend.getiverse.repository;

import com.backend.getiverse.model.PostType;
import com.backend.getiverse.model.Rating;
import com.backend.getiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    Rating findByid(String id);

    @Query("{'userId': ?0}")
    List<Rating> findByUserId(String userId);

    @Query("{'userId': ?0}")
    Page<Rating> findAllRatings(Pageable pageable);

    @Query("{'$and': [{'postId': ?0}, {'postType': ?1} ] }")
    Page<Rating> findByPostIdAndPostType(String postId, PostType postType, Pageable pageable);
    @Query("{'$and': [{'postId': ?0}, {'postType': ?1}, {'userId': ?2} ] }")
    Rating findByPostIdAndPostTypeAndUserId(String postId, PostType postType, String userId);
    @Query(value = "{'userId' : ?0  }", delete = true)
    void deleteByUid(String uid);
    @Query(value = "{'$and': [{'postId': ?0}, {'postType': ?1} ] }", delete = true)
    List<Rating> deleteByPostIdAndPostType(String postId, PostType postType);
}