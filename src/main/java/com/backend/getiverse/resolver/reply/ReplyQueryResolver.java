package com.backend.getiverse.resolver.reply;

import com.backend.getiverse.model.Rating;
import com.backend.getiverse.model.Reply;
import com.backend.getiverse.repository.ReplyRepository;
import com.backend.getiverse.resolver.pages.RepliesPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ReplyQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private ReplyRepository replyRepository;

    public RepliesPage findAllReplies(com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Reply> replies = replyRepository.findAllReplies(LocalDate.now(), LocalTime.now(),pageable);
        return new RepliesPage((int) replies.getTotalElements(), replies.getContent());
    }

    public RepliesPage findRepliesByRatingId(String ratingId, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Reply> replies = replyRepository.findByRatingId(ratingId, pageable);
        return new RepliesPage((int) replies.getTotalElements(), replies.getContent());
    }
    public Reply findReplyById(String id) {
        return replyRepository.findByid(id);
    }
}
