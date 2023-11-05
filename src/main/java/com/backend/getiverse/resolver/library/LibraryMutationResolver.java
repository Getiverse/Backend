package com.backend.getiverse.resolver.library;


import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.Article;
import com.backend.getiverse.model.Library;
import com.backend.getiverse.model.Reply;
import com.backend.getiverse.repository.LibraryRepository;
import com.backend.getiverse.resolver.pages.LibrariesPage;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
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
public class LibraryMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private LibraryRepository libraryRepository;

    public Boolean removeArticleFromLibrary(String libraryId, String articleId) {
        SecurityService sr = new SecurityService();
        Optional<Library> library = libraryRepository.findById(libraryId);
        String uid = sr.getUser().getUid();
        if (library.get() != null && library.get().getUserId().equals(uid)) {
            List<String> articles = library.get().getArticles();
            articles.remove(articleId);
            library.get().setArticles(articles);
            if (articles.size() == 0)
                library.get().setImage("");
            libraryRepository.save(library.get());
            return true;
        }
        return false;
    }


    public Boolean removeInstantFromLibrary(String libraryId, String instantId) {
        SecurityService sr = new SecurityService();
        Optional<Library> library = libraryRepository.findById(libraryId);
        String uid = sr.getUser().getUid();
        if (library.get() != null && library.get().getUserId().equals(uid)) {
            List<String> instants = library.get().getInstants();
            instants.remove(instantId);
            library.get().setInstants(instants);
            libraryRepository.save(library.get());
            return true;
        }
        return false;
    }
}
