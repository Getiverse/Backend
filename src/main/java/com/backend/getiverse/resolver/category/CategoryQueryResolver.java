package com.backend.getiverse.resolver.category;

import com.backend.getiverse.model.Category;
import com.backend.getiverse.repository.CategoryRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class CategoryQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAllCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }

    public Optional<Category> findCategoryById(String id){
        return categoryRepository.findById(id);
    }
}