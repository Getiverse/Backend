package com.backend.getiverse.repository;

import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Library;
import com.backend.getiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface LibraryRepository extends MongoRepository<Library, String> {
    Library findByid(String id);

    @Query("{ $and: [{'postDate' : { $lte: ?0 }, {'postTime' : { $lte: ?1 }] }")
    public Page<Library> findAllLibraries(LocalDate date, LocalTime time, Pageable pageable);

    @Query("{'userId' : ?0  }")
    Page<Library> findByUserId(String userId, Pageable pageable);

    @Query("{ $and: [{'userId' : ?0} , {'isPrivate' : ?1 }] }")
    Page<Library> findLibrariesByUserIdAndPrivateStatus(String uid, Boolean isPrivate, Pageable pageable);
    @Query(value = "{'userId' : ?0  }", delete = true)
    void deleteByUid(String uid);
}
