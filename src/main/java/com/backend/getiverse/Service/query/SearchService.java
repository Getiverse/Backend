package com.backend.getiverse.Service.query;

import com.backend.getiverse.controller.support.SearchPage;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Instant;
import com.backend.getiverse.model.User;
import com.backend.getiverse.repository.ArticleRepository;
import com.backend.getiverse.repository.InstantRepository;
import com.backend.getiverse.repository.UserRepository;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private InstantRepository instantRepository;

    public SearchPage searchInstant(String name, Pageable pageable) {
        Query textQuery = TextQuery.queryText(new TextCriteria().forDefaultLanguage().matchingAny(name)).sortByScore().with(pageable);
        List<Instant> instants = mongoTemplate.find(textQuery, Instant.class);
        return new SearchPage(instants, (int) mongoTemplate.count(textQuery.skip(0).limit(0), Instant.class));
    }

    public SearchPage searchUser(String name, Pageable pageable) {
        Query textQuery = TextQuery.queryText(new TextCriteria().forDefaultLanguage().matchingAny(name)).sortByScore().with(pageable);
        List<User> users = mongoTemplate.find(textQuery, User.class);
        return new SearchPage(users, (int) mongoTemplate.count(textQuery.skip(0).limit(0), User.class));
    }

    public SearchPage searchArticle(String name, Pageable pageable) {
        Query textQuery = TextQuery.queryText(new TextCriteria().forDefaultLanguage().matchingAny(name)).sortByScore().with(pageable);
        List<Article> articles = mongoTemplate.find(textQuery, Article.class);
        return new SearchPage(articles, (int) mongoTemplate.count(textQuery.skip(0).limit(0), Article.class));
    }
}
