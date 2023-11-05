package com.backend.getiverse.resolver.instant;

import com.backend.getiverse.Service.FileService;
import com.backend.getiverse.Service.query.ScoreCalculatorService;
import com.backend.getiverse.Service.query.SuggestInstantsService;
import com.backend.getiverse.enums.FilterType;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Instant;
import com.backend.getiverse.model.Page;
import com.backend.getiverse.repository.InstantRepository;
import com.backend.getiverse.repository.UserRepository;
import com.backend.getiverse.resolver.pages.InstantsPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class InstantQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private InstantRepository instantRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ScoreCalculatorService scoreCalculatorService;
    @Autowired
    private SuggestInstantsService suggestInstantsService;

    public InstantsPage findAllInstants(Page page, FilterType filterType, String filterValue) {
        SecurityService sr = new SecurityService();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<Instant> instantPage = suggestInstantsService.suggest(pageable, filterType, filterValue);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public Optional<Instant> findInstantById(String id) {
        Optional<Instant> instant = instantRepository.findById(id);
        if (!instant.isEmpty()) {
            instant.get().setViews(instant.get().getViews() + 1);
            if(instant.get().getViews() % 1000 == 0) {
                instant.get().setScore(scoreCalculatorService.calculateInstantScore(instant.get()));
            }
            instantRepository.save(instant.get());
        }
        return instant;
    }

    public InstantsPage getInstantsByCategory(String category, Page page) {
        SecurityService sr = new SecurityService();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.getInstantByCategory(category, sr.getUser().getUid(), pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public InstantsPage getInstantByRating(int rating, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.getInstantByRating(rating, pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public InstantsPage findInstantsByIds(List<String> ids, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.findByIds(ids, pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public InstantsPage getInstantsByCategories(List<String> categories, Page page) {
        SecurityService sr = new SecurityService();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        ArrayList<String> arr = new ArrayList<>();
        arr.addAll(categories);
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.getInstantsByCategories(arr, sr.getUser().getUid(), pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public InstantsPage getInstantsByUserIds(List<String> userIds, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        ArrayList<String> arr = new ArrayList<>();
        arr.addAll(userIds);
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.getInstantsByUserIds(arr, pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public InstantsPage getInstantsByUserId(String userId, Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.getInstantsByUserIdWithPageable(userId, pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }
}
