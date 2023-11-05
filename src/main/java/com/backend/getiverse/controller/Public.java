package com.backend.getiverse.controller;

import com.backend.getiverse.Service.query.SearchService;
import com.backend.getiverse.controller.support.RequestData;
import com.backend.getiverse.controller.support.SearchPage;
import com.backend.getiverse.controller.support.SearchType;
import com.backend.getiverse.enums.FilterType;
import com.backend.getiverse.model.*;
import com.backend.getiverse.repository.ArticleRepository;
import com.backend.getiverse.repository.CategoryRepository;
import com.backend.getiverse.repository.InstantRepository;
import com.backend.getiverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("getiverse.vercel.app")
@RequestMapping("public")
public class Public {
    @Autowired
    private SearchService searchService;

    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private InstantRepository instantRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("/instant/{id}")
    public Optional<Instant> instant(@PathVariable String id) throws IOException {
        Optional<Instant> instant = instantRepository.findById(id);
        return instant;
    }

    @GetMapping("/article/{id}")
    public Optional<Article> article(@PathVariable String id) throws IOException {
        Optional<Article> article = articleRepo.findArticleById(id);
        return article;
    }

    @GetMapping("/user/{id}")
    public Optional<User> user(@PathVariable String id) throws IOException {
        Optional<User> user = userRepo.findUserById(id);
        return user;
    }

    @GetMapping("/categories")
    public List<Category> categories() throws IOException {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }

    @PostMapping("/search")
    public SearchPage search(@RequestBody RequestData body) throws IOException {
        if(body.getName() == null) return null;
        Pageable pageable = PageRequest.of(body.getPage().getPage(), body.getPage().getSize());
        if (body.getSearchType().equals(SearchType.ARTICLE))
            return searchService.searchArticle(body.getName(), pageable);
        if (body.getSearchType().equals(SearchType.INSTANT))
            return searchService.searchInstant(body.getName(), pageable);
        return searchService.searchUser(body.getName(), pageable);
    }
}
