package com.backend.getiverse.resolver.library;


import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Instant;
import com.backend.getiverse.model.Library;
import com.backend.getiverse.repository.LibraryRepository;
import com.backend.getiverse.resolver.pages.LibrariesPage;
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
import java.util.Optional;

@AllArgsConstructor
@Component
public class LibraryQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LibraryRepository libraryRepository;

    public Library findLibraryById(String id){
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        Library library = libraryRepository.findByid((id));
        if(userId.equals(library.getUserId())) {
            return library;
        }
        if(!library.getIsPrivate()){
            return library;
        }
        return null;
    }

    public LibrariesPage findAllLibraries(com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<Library> LibraryPage = libraryRepository.findAllLibraries(LocalDate.now(), LocalTime.now(),pageable);
        return new LibrariesPage((int) LibraryPage.getTotalElements(), LibraryPage.getContent());
    }
    public LibrariesPage findLibrariesByUserId(String uid, com.backend.getiverse.model.Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<Library> LibraryPage = libraryRepository.findLibrariesByUserIdAndPrivateStatus(uid, false, pageable);
        return new LibrariesPage((int) LibraryPage.getTotalElements(), LibraryPage.getContent());
    }
}
