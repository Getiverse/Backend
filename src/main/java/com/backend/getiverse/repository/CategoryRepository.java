package com.backend.getiverse.repository;

import com.backend.getiverse.model.Category;
import com.backend.getiverse.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category,String> {
    @Query("{'id' : {$in: ?0} }")
    List<Category> findAll(List<String> selectedCategories);
}
