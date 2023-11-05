package com.backend.getiverse.resolver.me;

import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.model.*;
import com.backend.getiverse.repository.*;
import com.backend.getiverse.resolver.pages.ArticlesPage;
import com.backend.getiverse.resolver.pages.InstantsPage;
import com.backend.getiverse.resolver.pages.LibrariesPage;
import com.backend.getiverse.resolver.pages.UsersPage;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
/**
 * User must be logged in
 */
public class MeQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private RatingRepository ratingRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private InstantRepository instantRepository;
    @Autowired
    private LibraryRepository libraryRepo;

    /**
     * current user logged in Follow information
     *
     * @return followed users
     */
    public UsersPage getFollow(Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        User userFound = userRepo.findById(userId).get();
        List<String> followedUsers = userFound.getFollow();
        if(followedUsers.size() == 0) return new UsersPage(0, new ArrayList<>());
        if(followedUsers.size() == 1){
            User userFollowed = userRepo.findById(followedUsers.get(0)).get();
            ArrayList<User> temp = new ArrayList<>();
            temp.add(userFollowed);
            return new UsersPage(1, temp);
        }
        org.springframework.data.domain.Page<User> userPage = userRepo.getUsersByUserIds(followedUsers, pageable);
        return new UsersPage((int) userPage.getTotalElements(), userPage.getContent());
    }


    public UsersPage getFollowers(Page page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        User userFound = userRepo.findById(userId).get();

        List<String> followers = userFound.getFollowers();
        if(followers.size() == 0) return new UsersPage(0, new ArrayList<>());
        if(followers.size() == 1) userRepo.findById(followers.get(0));
        if(followers.size() == 1){
            User follower = userRepo.findById(followers.get(0)).get();
            ArrayList<User> temp = new ArrayList<>();
            temp.add(follower);
            return new UsersPage(1, temp);
        }
        org.springframework.data.domain.Page<User> userPage = userRepo.getUsersByUserIds(followers, pageable);
        return new UsersPage((int) userPage.getTotalElements(), userPage.getContent());
    }

    public int getPostViews() {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        return 0;
    }

    /**
     * Current user logged in information
     *
     * @return all the information about current user logged in
     */
    public User me() {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        return userRepo.findById(userId).get();
    }

    /**
     * Returns all the Rating that the user's Articles and Instants has received
     *
     * @return Rating info of Instants and Posts of the current user logged in
     */
    public List<Rating> getRating() {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        return ratingRepo.findByUserId(userId);
    }

    public List<Category> getCategories() {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        List<String> selectedCategories = userRepo.findById(userId).get().getSelectedCategories();
        return categoryRepo.findAll(selectedCategories);
    }

    public ArticlesPage getMyArticles(Page page) {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        org.springframework.data.domain.Page<Article> articlePage = articleRepo.getArticlesByUserId(userId, pageable);
        return new ArticlesPage((int) articlePage.getTotalElements(), articlePage.getContent());
    }

    public InstantsPage getMyInstants(Page page) {
        SecurityService sr = new SecurityService();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize()).withSort(Sort.by("postTime").and(Sort.by("createdAt")).descending());
        String userId = sr.getUser().getUid();
        org.springframework.data.domain.Page<Instant> instantPage = instantRepository.getInstantsByUserId(userId, pageable);
        return new InstantsPage((int) instantPage.getTotalElements(), instantPage.getContent());
    }

    public LibrariesPage getMyLibraries(Page page) {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        List<Library> libraries = new ArrayList<Library>();
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        org.springframework.data.domain.Page<Library> libraryPage = libraryRepo.findByUserId(userId, pageable);
        return new LibrariesPage((int) libraryPage.getTotalElements(), libraryPage.getContent());
    }

    public Library findMyLibraryById(String id) {
        SecurityService sr = new SecurityService();
        String userId = sr.getUser().getUid();
        Library library = libraryRepo.findByid((id));
        if (userId.equals(library.getUserId())) {
            return library;
        }
        return null;
    }
}
