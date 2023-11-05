package com.backend.getiverse.repository;

import com.backend.getiverse.model.Library;
import com.backend.getiverse.model.Reports;
import com.backend.getiverse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReportRepository extends MongoRepository<Reports,String> {
    Reports findByid(String id);

    @Query("$and: [{'postDate' : { $lte: ?0 }, {'postTime' : { $lte: ?1 }] }")
    public Page<Reports> findAllReports(LocalDate date, LocalTime time, Pageable pageable);

}
