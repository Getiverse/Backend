package com.backend.getiverse.repository;

import com.backend.getiverse.model.Library;
import com.backend.getiverse.model.Rating;
import com.backend.getiverse.model.Reply;
import com.backend.getiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReplyRepository extends MongoRepository<Reply,String> {
    Reply findByid(String id);

    @Query("$and: [{'postDate' : { $lte: ?0 }, {'postTime' : { $lte: ?1 }] }")
    public Page<Reply> findAllReplies(LocalDate date, LocalTime time, Pageable pageable);
    @Query("{'ratingId' : ?0 }")
    Page<Reply> findByRatingId(String ratingId, Pageable pageable);
    @Query(value = "{'userId' : ?0  }", delete = true)
    void deleteByUid(String uid);
    @Query(value = "{'ratingId' : ?0  }", delete = true)
    void deleteByRatingId(String ratingId);
}
