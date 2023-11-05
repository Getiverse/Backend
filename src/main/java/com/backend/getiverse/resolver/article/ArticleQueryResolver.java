package com.backend.getiverse.resolver.article;

import com.backend.getiverse.Service.query.ScoreCalculatorService;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Article;
import com.backend.getiverse.repository.ArticleRepository;
import com.backend.getiverse.resolver.pages.ArticlesPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ArticleQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private ScoreCalculatorService scoreCalculatorService;

    public ArticlesPage findAllArticles(com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Article> posts = articleRepo.findAllArticles(LocalDate.now(), LocalTime.now(), pageable);

        return new ArticlesPage((int) posts.getTotalElements(), posts.getContent());
    }

    public Optional<Article> findArticleById(String id) {
        Optional<Article> article = articleRepo.findArticleById(id);
        if (!article.isEmpty()) {
            article.get().setViews(article.get().getViews() + 1);
            if(article.get().getViews() % 1000 == 0) {
                article.get().setScore(scoreCalculatorService.calculateArticleScore(article.get()));
            }
            articleRepo.save(article.get());
        }
        return article;
    }

    public ArticlesPage getArticlesByCategories(List<String> categories, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        SecurityService sr = new SecurityService();
        ArrayList<String> arr = new ArrayList<>();
        arr.addAll(categories);
        Page<Article> articlePage = articleRepo.getArticlesByCategories(arr, sr.getUser().getUid(), pageable);
        return new ArticlesPage((int) articlePage.getTotalElements(), articlePage.getContent());
    }

    public ArticlesPage getArticlesByCategory(String category, com.backend.getiverse.model.Page page) {
        SecurityService sr = new SecurityService();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Article> articlePage = articleRepo.getArticlesByCategory(category, sr.getUser().getUid(), pageable);
        return new ArticlesPage((int) articlePage.getTotalElements(), articlePage.getContent());
    }

    public ArticlesPage getArticlesByUserIds(List<String> userId, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        ArrayList<String> arr = new ArrayList<>();
        arr.addAll(userId);
        Page<Article> articlePage = articleRepo.getArticlesByUserIds(arr, pageable);
        return new ArticlesPage((int) articlePage.getTotalElements(), articlePage.getContent());
    }

    public ArticlesPage getArticlesByUserId(String userId, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        Page<Article> articlePage = articleRepo.getArticlesByUserId(userId, pageable);
        return new ArticlesPage((int) articlePage.getTotalElements(), articlePage.getContent());
    }

    public List<Article> getArticlesByRating(int rating) {
        List<Article> postList = articleRepo.getArticlesByRating(rating);
        return postList;
    }

    public ArticlesPage findArticlesByIds(List<String> ids, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        Page<Article> articlePage = articleRepo.findByIds(ids, pageable);
        return new ArticlesPage((int) articlePage.getTotalElements(), articlePage.getContent());

    }
}