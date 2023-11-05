package com.backend.getiverse.repository;

import com.backend.getiverse.model.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface InstantRepository extends MongoRepository<Instant, String> {
    @Query("{ $and: [{'categories' : ?0 }, {'userId' : { $ne: ?1 }}] }")
    public Page<Instant> getInstantByCategory(String category, String uid, Pageable pageable);

    @Query("{'ratingAverage' : ?0 }")
    public Page<Instant> getInstantByRating(int rating, Pageable pageable);

    @Query("{'userId' : {$in: ?0} }")
    Page<Instant> getInstantByUserIds(List<String> userId, Pageable pageable);

    @Query("{'userId' : {$in: ?0} }")
    Page<Instant> getInstantsByUserIds(String userIds, Pageable pageable);

    Instant findByid(String id);

    @Query("{'userId' : ?0 }")
    Page<Instant> getInstantsByUserId(String userId, Pageable pageable);

    @Query("{'userId' : {$in :?0} }")
    Page<Instant> getInstantsByUserIds(ArrayList<String> arr, Pageable pageable);

    @Query("{ $and: [{'categories' : {$in :?0 } }, {'userId' : { $ne: ?1 }}] }")
    Page<Instant> getInstantsByCategories(ArrayList<String> arr, String uid, Pageable pageable);

    @Query("{'id' : {$in :?0} }")
    Page<Instant> findByIds(List<String> ids, Pageable pageable);
    @Query("{'userId' : ?0 }")
    Page<Instant> getInstantsByUserIdWithPageable(String userId, Pageable pageable);
    @Query(value = "{'userId' : ?0  }", delete = true)
    void deleteByUid(String uid);
}