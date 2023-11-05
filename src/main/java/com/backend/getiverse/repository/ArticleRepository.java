package com.backend.getiverse.repository;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Instant;
import com.backend.getiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {
    @Query("{ $and: [{'categories' : ?0 }, {'userId' : { $ne: ?1 }}] }")
    public Page<Article> getArticlesByCategory(String category, String uid, Pageable pageable);

    @Query("{'ratingAverage' : ?0 }")
    public List<Article> getArticlesByRating(int rating);

    Optional<Article> findArticleById(String id);

    @Query("{'userId' : {$in :?0} }")
    public Page<Article> getArticlesByUserIds(List<String> userId, Pageable pageable);

    @Query("{'userId' : ?0 }")
    public Page<Article> getArticlesByUserId(String userId, Pageable pageable);

    @Query("{ $and: [{'postDate' : { $lte: ?0 }, {'postTime' : { $lte: ?1 }}] }")
    public Page<Article> findAllArticles(LocalDate date, LocalTime time, Pageable pageable);

    Article findByid(String id);

    @Query("{ $and: [{'categories' : {$in :?0 } }, {'userId' : { $ne: ?1 }}] }")
    Page<Article> getArticlesByCategories(ArrayList<String> arr, String uid, Pageable pageable);

    @Query("{'id' : {$in :?0} }")
    Page<Article> findByIds(List<String> ids, Pageable pageable);

    @Query(value = "{'userId' : ?0  }", delete = true)
    void deleteByUid(String uid);

    @Query("{ $text : { $search :?0 } }")
    Page<Article> search(String name, Pageable pageable);
}

