package com.backend.getiverse.Service.query;

import com.backend.getiverse.enums.FilterType;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Instant;
import com.backend.getiverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestInstantsService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepo;

    public Page<Instant> suggest(Pageable pageable, FilterType filterType, String filterValue) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        List<String> usersIdFilter = new ArrayList<>();
        ArrayList<String> categories = new ArrayList();
        if (userId != null) {
        }
        if (userId != null) {
            categories.addAll(userRepo.findById(userId).get().getSelectedCategories());
            usersIdFilter.add(userId);
        }
        if (filterType != null && filterType.equals(FilterType.INSTANT_ID) && filterValue != null) {
            criteria.add(Criteria.where("id").ne(filterValue));
        } else if (filterType != null && filterType.equals(FilterType.LIBRARY_ID) && filterValue != null) {
            criteria.add(Criteria.where("libraryId").ne(filterValue));
        } else if (filterType != null && filterType.equals(FilterType.USER_ID) && filterValue != null) {
            usersIdFilter.add(filterValue);
            criteria.add(Criteria.where("userId").nin(usersIdFilter));
            if (categories.size() > 1)
                criteria.add(Criteria.where("categories").in(categories));
            else criteria.add(Criteria.where("categories").is(categories.get(0)));
        } else if (filterType == null && filterValue == null && userId != null) {
            criteria.add(Criteria.where("userId").ne(userId));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        Page<Instant> instants = PageableExecutionUtils.getPage(mongoTemplate.find(query, Instant.class), pageable, () -> mongoTemplate.count(query.skip(0).limit(0), Instant.class));
        return instants;
    }

}
