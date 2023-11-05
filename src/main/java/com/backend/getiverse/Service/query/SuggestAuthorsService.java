package com.backend.getiverse.Service.query;

import com.backend.getiverse.enums.FilterType;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Instant;
import com.backend.getiverse.model.User;
import com.backend.getiverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestAuthorsService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepo;

    public Page<User> suggest(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        criteria.add(Criteria.where("id").ne(userId));
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        Page<User> users = PageableExecutionUtils.getPage(mongoTemplate.find(query, User.class), pageable, () -> mongoTemplate.count(query.skip(0).limit(0), User.class));
        return users;
    }
}
