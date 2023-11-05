package com.backend.getiverse.resolver.report;

import com.backend.getiverse.model.Library;
import com.backend.getiverse.model.Reports;
import com.backend.getiverse.repository.ReportRepository;
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
public class ReportQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private ReportRepository reportRepository;

    public List<Reports> findAllReports(com.backend.getiverse.model.Page page) {
        List<Reports> reports = new ArrayList<>();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Reports> reports1 = reportRepository.findAllReports(LocalDate.now(), LocalTime.now(),pageable);
        reports = reports1.getContent();
        return reports;
    }
}
