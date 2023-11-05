package com.backend.getiverse.repository;

import com.backend.getiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'id' : {$in: ?0} }")
    public Page<User> getUsersByUserIds(List<String> userId, Pageable pageable);

    @Query("{'userName': {$eq: ?0} }")
    User findByUsername(String userName);

    Optional<User> findUserById(String id);
}