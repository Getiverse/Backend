package com.backend.getiverse.resolver.me;

import com.backend.getiverse.firebase.auth.SecurityConfig;
import com.backend.getiverse.firebase.auth.SecurityFilter;
import com.backend.getiverse.firebase.auth.SecurityService;
import com.backend.getiverse.firebase.utils.CookieUtils;
import com.backend.getiverse.input.*;
import com.backend.getiverse.input.update.*;
import com.backend.getiverse.model.*;
import com.backend.getiverse.repository.*;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import graphql.GraphQLException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class MeMutationResolver implements GraphQLMutationResolver {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private InstantRepository instantRepo;
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    CookieUtils cookieUtils;

    public User login() {
        SecurityService sr = new SecurityService();
        com.backend.getiverse.firebase.auth.models.User user = sr.getUser();
        Optional<User> userState = userRepo.findById(user.getUid());
        if (userState.isEmpty()) {
            String idReadLater = libraryRepository.save(new Library(user.getUid(), true, "", "Read Later", "Posts saved which you could read later")).getId();
            String idFavourites = libraryRepository.save(new Library(user.getUid(), true, "", "Favorite", "Favorite Posts")).getId();
            String userImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png";
            if (user.getPicture() != null) {
                userImage = user.getPicture();
            }
            User userCreated = new User(user.getName(), userImage,
                    "", "");
            User foundUserByUserName = userRepo.findByUsername(user.getName());
            if (foundUserByUserName == null) {
                userCreated.setUserName(user.getName());
            } else {
                int unique_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                userCreated.setUserName(user.getName() + "_" + unique_id);
            }
            ArrayList<String> defaultLibraries = new ArrayList<>();
            defaultLibraries.add(idFavourites);
            defaultLibraries.add(idReadLater);
            userCreated.setSaved(defaultLibraries);
            return userRepo.save(userCreated);
        }
        User foundUser = userState.get();
        return foundUser;
    }

    public User logout() throws FirebaseAuthException {
        SecurityService sr = new SecurityService();
        com.backend.getiverse.firebase.auth.models.User user = sr.getUser();
        User foundUser = userRepo.findById(user.getUid()).get();
        return foundUser;
    }

    public User deleteUser() throws FirebaseAuthException {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        articleRepo.deleteByUid(uid);
        instantRepo.deleteByUid(uid);
        libraryRepository.deleteByUid(uid);
        ratingRepository.deleteByUid(uid);
        replyRepository.deleteByUid(uid);
        User foundUser = userRepo.findById(uid).get();
        userRepo.deleteById(uid);
        return foundUser;
    }

    public String follow(String userId) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        User followedUser = userRepo.findById((userId)).get();
        ArrayList<String> follows = new ArrayList<>(me.getFollow());
        if (follows.contains(userId)) {
            follows.remove(userId);
        } else {
            follows.add(userId);
        }
        if (followedUser.getFollowers().contains(me.getId())) {
            followedUser.getFollowers().remove(me.getId());
        } else {
            followedUser.getFollowers().add(me.getId());
        }
        me.setFollow(follows);
        userRepo.save(followedUser);
        userRepo.save(me);
        return userId;
    }

    public Rating setUsefulRating(String ratingId) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Rating rating = ratingRepository.findByid(ratingId);
        ArrayList<String> useFulList = new ArrayList<>(rating.getUsefulRating());
        if (useFulList.contains(uid)) {
            useFulList.remove(uid);
        } else {
            useFulList.add(uid);
        }
        rating.setUsefulRating(useFulList);
        ratingRepository.save(rating);
        return rating;
    }

    public Rating setNotUsefulRating(String ratingId) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Rating rating = ratingRepository.findByid(ratingId);
        ArrayList<String> useFulList = new ArrayList<>(rating.getNotUsefulRating());
        if (useFulList.contains(uid)) {
            useFulList.remove(uid);
        } else {
            useFulList.add(uid);
        }
        rating.setNotUsefulRating(useFulList);
        ratingRepository.save(rating);
        return rating;
    }

    public Reply setUsefulReply(String replyId) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Reply reply = replyRepository.findByid(replyId);
        ArrayList<String> useFulList = new ArrayList<>(reply.getUsefulRating());
        if (useFulList.contains(uid)) {
            useFulList.remove(uid);
        } else {
            useFulList.add(uid);
        }
        reply.setUsefulRating(useFulList);
        replyRepository.save(reply);
        return reply;
    }

    public Reply setNotUsefulReply(String replyId) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Reply reply = replyRepository.findByid(replyId);
        ArrayList<String> useFulList = new ArrayList<>(reply.getNotUsefulRating());
        if (useFulList.contains(uid)) {
            useFulList.remove(uid);
        } else {
            useFulList.add(uid);
        }
        reply.setNotUsefulRating(useFulList);
        replyRepository.save(reply);
        return reply;
    }

    public List<String> selectCategory(String categoryId) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        ArrayList<String> categories = new ArrayList(me.getSelectedCategories());
        if (categories.contains(categoryId)) {
            categories.remove(categoryId);
        } else {
            categories.add(categoryId);
        }
        me.setSelectedCategories(categories);
        userRepo.save(me);
        return categories;
    }

    public User updateMyProfile(UpdateUserInput input) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        User userByUsername = userRepo.findByUsername(input.getUserName());
        User user = userRepo.findById(userID).get();
        if (userByUsername == null || input.getUserName().equals(user.getUserName())) {
            user.setName(input.getName());
            user.setProfileImage(input.getProfileImage());
            user.setBackgroundImage(input.getBackgroundImage());
            user.setBio(input.getBio());
            user.setLinks(input.getLinks());
            user.setSocialLinks(input.getSocialLinks());
            user.setContact(input.getContact());
            return userRepo.save(user);
        } else {
            throw new GraphQLException("Username Already Taken");
        }
    }

    public Library addLibrary(CreateLibraryInput input) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        Library library = new Library(uid, input.getIsPrivate(),
                "", input.getTitle(), input.getDescription());
        libraryRepository.save(library);
        me.getSaved().add(library.getId());
        userRepo.save(me);
        return library;
    }

    public Optional<Library> removeLibrary(String id) {
        Optional<Library> removedElement = null;
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();

        User me = userRepo.findById(uid).get();
        Optional<Library> i = libraryRepository.findById(id);
        me.getSaved().remove(i.get().getId());
        userRepo.save(me);
        if (uid.equals(i.get().getUserId())) {
            removedElement = i;
            libraryRepository.deleteById(i.get().getId());
        }
        return removedElement;
    }

    public Library updateLibrary(UpdateLibraryInput input) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        Library library = libraryRepository.findByid(input.getId());
        if (userID.equals(library.getUserId()) && library != null) {
            library.setTitle(input.getTitle());
            library.setIsPrivate(input.getIsPrivate());
            library.setDescription(input.getDescription());
            return libraryRepository.save(library);
        }
        return null;
    }

    public boolean saveTo(List<String> libraryIds, String postId, PostType postType) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        for (String libraryId : libraryIds) {
            Library library = libraryRepository.findByid(libraryId);
            if (userID.equals(library.getUserId()) && library != null) {
                if (postType.equals(PostType.ARTICLE)) {
                    Article article = articleRepo.findByid(postId);
                    if (!library.getArticles().contains(article.getId())) {
                        library.getArticles().add(article.getId());
                        library.setImage(article.getImage());
                    }
                } else if (postType.equals(PostType.INSTANT)) {
                    Instant instant = instantRepo.findByid(postId);
                    if (!library.getInstants().contains(instant.getId()))
                        library.getInstants().add(instant.getId());
                }
                libraryRepository.save(library);
            }
        }
        return true;
    }


    public Article addArticle(CreateArticleInput input) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        Article article = new Article(me.getId(), input.getImage(), input.getTitle(), input.getContent(),
                input.getCategories(),
                LocalDate.parse(input.getPostDate()), LocalTime.parse(input.getPostTime()), input.getReadTime());
        Article articleResult = articleRepo.save(article);
        me.setNumberOfArticles(me.getNumberOfArticles() + 1);
        userRepo.save(me);
        return articleResult;
    }

    public Optional<Article> removeArticle(String id) {
        Optional<Article> removedElement = null;
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        Optional<Article> i = articleRepo.findById(id);
        if (uid.equals(i.get().getUserId())) {
            removedElement = i;
            articleRepo.deleteById(id);
        }
        List<Rating> ratings = ratingRepository.deleteByPostIdAndPostType(id, PostType.ARTICLE);
        for (Rating rating : ratings) {
            replyRepository.deleteByRatingId(rating.getId());
        }
        me.setNumberOfArticles(me.getNumberOfArticles() - 1);
        userRepo.save(me);
        return removedElement;
    }

    public Article updateArticle(UpdateArticleInput input) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        Article article = articleRepo.findByid(input.getId());
        article.setCategories(input.getCategories());
        article.setContent(input.getContent());
        article.setImage(input.getImage());
        article.setTitle(input.getTitle());
        return articleRepo.save(article);
    }

    public Instant addInstant(CreateInstantInput input) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        Instant instant = new Instant(me.getId(), input.getTitle(),
                input.getContent(), input.getImage(),
                input.getCategories(), LocalDate.parse(input.getPostDate()),
                LocalTime.parse(input.getPostTime()));
        Instant instantResult = instantRepo.save(instant);
        me.setNumberOfInstants(me.getNumberOfInstants() + 1);
        userRepo.save(me);
        return instantResult;
    }

    public Optional<Instant> removeInstant(String id) {
        Optional<Instant> removedElement = null;
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        Optional<Instant> i = instantRepo.findById(id);
        if (uid.equals(i.get().getUserId())) {
            removedElement = i;
            instantRepo.deleteById(id);
        }
        List<Rating> ratings = ratingRepository.deleteByPostIdAndPostType(id, PostType.INSTANT);
        for (Rating rating : ratings) {
            replyRepository.deleteByRatingId(rating.getId());
        }
        me.setNumberOfInstants(me.getNumberOfInstants() - 1);
        userRepo.save(me);
        return removedElement;
    }

    public Instant updateInstant(UpdateInstantInput input) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        Instant instant = instantRepo.findByid(input.getId());
        instant.setCategories(input.getCategories());
        instant.setContent(input.getContent());
        instant.setTitle(input.getTitle());
        instant.setImage(input.getImage());
        return instantRepo.save(instant);
    }

    public Rating addRating(CreateRatingInput input) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Rating rating = new Rating(uid, input.getStars(),
                input.getComment(), input.getPostId(), input.getPostType());
        Rating ratingSaved = ratingRepository.save(rating);
        if (input.getPostType() == PostType.ARTICLE) {
            Article article = articleRepo.findByid(input.getPostId());
            article.setRatingSum(article.getRatingSum() + input.getStars());
            article.setRatingsNumber(article.getRatingsNumber() + 1);
            articleRepo.save(article);
        } else {
            Instant instant = instantRepo.findByid(input.getPostId());
            instant.setRatingSum(instant.getRatingSum() + input.getStars());
            instant.setRatingsNumber(instant.getRatingsNumber() + 1);
            instantRepo.save(instant);
        }
        return ratingSaved;
    }

    public Optional<Rating> removeRating(String id) {
        Optional<Rating> removedElement = null;
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Optional<Rating> rating = ratingRepository.findById(id);
        if (!uid.equals(rating.get().getUserId())) {
            throw new GraphQLException("can't delete others ratings");
        }
        int ratingSum = 0;
        if (rating.get().getPostType() == PostType.ARTICLE) {
            Article article = articleRepo.findByid(rating.get().getPostId());
            if (article.getRatingsNumber() > 1) {
                ratingSum = article.getRatingSum() - rating.get().getStars();
            }
            article.setRatingSum(ratingSum);
            article.setRatingsNumber(article.getRatingsNumber() - 1);
            articleRepo.save(article);
        } else {
            Instant instant = instantRepo.findByid(rating.get().getPostId());
            if (instant.getRatingsNumber() > 1) {
                ratingSum = instant.getRatingSum() - rating.get().getStars();
            }
            instant.setRatingSum(ratingSum);
            instant.setRatingsNumber(instant.getRatingsNumber() - 1);
            instantRepo.save(instant);
        }
        removedElement = rating;
        replyRepository.deleteByRatingId(id);
        ratingRepository.deleteById(id);
        return removedElement;
    }


    public Rating editRating(UpdateRatingInput input) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        Rating my_rating = ratingRepository.findByPostIdAndPostTypeAndUserId(input.getPostId(), input.getPostType(), userID);
        if (input.getPostType() == PostType.ARTICLE) {
            Article article = articleRepo.findArticleById(input.getPostId()).get();
            article.setRatingSum(article.getRatingSum() + (input.getStars() - my_rating.getStars()));
            articleRepo.save(article);
        } else {
            Instant instant = instantRepo.findByid((input.getPostId()));
            instant.setRatingSum(instant.getRatingSum() + (input.getStars() - my_rating.getStars()));
            instantRepo.save(instant);
        }
        my_rating.setComment(input.getComment());
        my_rating.setStars(input.getStars());
        return ratingRepository.save(my_rating);
    }

    public Reply addReply(CreateReplyInput input) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Reply reply = new Reply(uid, input.getRepliedUser(),
                input.getRatingId(), input.getComment());
        return replyRepository.save(reply);
    }

    public Optional<Reply> removeReply(String id) {
        Optional<Reply> removedElement = null;
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        Optional<Reply> reply = replyRepository.findById(id);
        if (uid.equals(reply.get().getUserId())) {
            removedElement = reply;
            replyRepository.deleteById(id);
        }
        return removedElement;
    }

    public Reply editReply(UpdateReplyInput input) {
        SecurityService sr = new SecurityService();
        String userID = sr.getUser().getUid();
        Reply reply = replyRepository.findByid(input.getReplyId());
        if (reply.getUserId().equals(userID)) {
            reply.setComment(input.getComment());
            return replyRepository.save(reply);
        }
        return null;
    }

    public Reports addReport(CreateReportInput input) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        Reports report = new Reports(uid, input.getType(), input.getPostType(), input.getPostId());
        userRepo.save(me);
        return reportRepository.save(report);
    }

    public User blockUser(String id) {
        SecurityService sr = new SecurityService();
        String uid = sr.getUser().getUid();
        User me = userRepo.findById(uid).get();
        if (me.getBlockedUsers().contains(id)) {
            me.getBlockedUsers().remove(id);
        } else {
            me.getBlockedUsers().add(id);
        }
        return userRepo.save(me);
    }
}
