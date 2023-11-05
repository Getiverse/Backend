package com.backend.getiverse.Service.query;

import com.backend.getiverse.model.*;
import com.backend.getiverse.repository.ArticleRepository;
import com.backend.getiverse.repository.InstantRepository;
import com.backend.getiverse.repository.RatingRepository;
import com.backend.getiverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ScoreCalculatorService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private InstantRepository instantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;

    private float weight_views = 0.1f;
    private float weight_rating_sum = 0.2f;
    private float weight_rating_numbers = 0.2f;
    private float weight_useful_ratings = 0.2f;
    private float weight_not_useful_ratings = 0.3f;

    public Float calculateArticleScore(Article article) {
        List<Article> userArticles = articleRepository.getArticlesByUserId(article.getUserId(), PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        int min_views = Integer.MAX_VALUE;
        int max_views = 0;
        int max_ratingSum = 0;
        int min_ratingSum = Integer.MAX_VALUE;
        int min_ratingNumbers = Integer.MAX_VALUE;
        int max_ratingNumbers = 0;

        for (Article userArticle : userArticles) {
            if (userArticle.getViews() > max_views) {
                max_views = userArticle.getViews();
            }
            if (userArticle.getViews() < min_views) {
                min_views = userArticle.getViews();
            }
            if (userArticle.getRatingsNumber() > max_ratingNumbers) {
                max_ratingNumbers = userArticle.getRatingsNumber();
            }
            if (userArticle.getRatingsNumber() < min_ratingNumbers) {
                min_ratingNumbers = userArticle.getRatingsNumber();
            }
        }
        List<Rating> ratings = ratingRepository.findByPostIdAndPostType(article.getId(), PostType.ARTICLE, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        int usefulRatingCount = 0;
        int notUsefulRatingCount = 0;
        int min_usefulRatingCount = Integer.MAX_VALUE;
        int max_usefulRatingCount = 0;
        int min_notUsefulRatingCount = Integer.MAX_VALUE;
        int max_notUsefulRatingCount = 0;

        for (Rating rating : ratings) {
            usefulRatingCount += rating.getUsefulRating().size();
            notUsefulRatingCount += rating.getUsefulRating().size();
            if (rating.getUsefulRating().size() > max_usefulRatingCount) {
                max_usefulRatingCount = rating.getUsefulRating().size();
            }
            if (rating.getUsefulRating().size() < min_usefulRatingCount) {
                min_usefulRatingCount = rating.getUsefulRating().size();
            }
            if (rating.getNotUsefulRating().size() > max_notUsefulRatingCount) {
                max_notUsefulRatingCount = rating.getNotUsefulRating().size();
            }
            if (rating.getUsefulRating().size() < min_notUsefulRatingCount) {
                min_notUsefulRatingCount = rating.getNotUsefulRating().size();
            }
        }
        float normalized_views = normalize(article.getViews(), min_views, max_views);
        float normalized_rating_sum = normalize(article.getRatingSum(), min_ratingSum, max_ratingSum);
        float normalized_rating_numbers = normalize(article.getRatingsNumber(), min_ratingNumbers, max_ratingNumbers);
        float normalized_useful_ratings = normalize(usefulRatingCount, min_usefulRatingCount, max_usefulRatingCount);
        float normalized_not_useful_ratings = normalize(notUsefulRatingCount, min_notUsefulRatingCount, max_notUsefulRatingCount);
        return (
                weight_views * normalized_views +
                        weight_rating_sum * normalized_rating_sum +
                        weight_rating_numbers * normalized_rating_numbers +
                        weight_useful_ratings * normalized_useful_ratings +
                        weight_not_useful_ratings * normalized_not_useful_ratings
        );
    }


    public Float calculateUserScore(User user) {
        if (user == null) return 0.0f;
        int min_views = Integer.MAX_VALUE;
        int max_views = 0;
        float min_instant_score = Float.MAX_VALUE;
        float max_instant_score = 0.0f;
        float min_article_score = Float.MAX_VALUE;
        float max_article_score = 0.0f;
        List<User> users = userRepository.findAll();
        for (User userProfile : users) {
            if (userProfile.getViews() > max_views) {
                max_views = userProfile.getViews();
            }
            if (userProfile.getViews() < min_views) {
                min_views = userProfile.getViews();
            }
        }
        List<Article> articles = articleRepository.getArticlesByUserId(user.getId(), PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<Instant> instants = instantRepository.getInstantsByUserId(user.getId(), PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        float articlesTotalScore;
        float instantsTotalScore;
        articlesTotalScore = 0.0f;
        instantsTotalScore = 0.0f;
        for (Article article : articles) {
            articlesTotalScore += article.getScore();
            if (article.getScore() - max_article_score > Float.MAX_VALUE) {
                max_article_score = article.getScore();
            }
            if (article.getScore() - min_article_score < Float.MIN_VALUE) {
                min_article_score = article.getScore();
            }

        }
        for (Instant instant : instants) {
            instantsTotalScore += instant.getScore();
            if (instant.getScore() - max_instant_score > Float.MAX_VALUE) {
                max_article_score = instant.getScore();
            }
            if (instant.getScore() - min_instant_score < Float.MIN_VALUE) {
                min_article_score = instant.getScore();
            }
            instantsTotalScore += instant.getScore();
        }
        float normalized_profile_views = normalize(user.getViews(), min_views, max_views);
        float normalized_total_article_score = normalize(articlesTotalScore, min_article_score, max_article_score);
        float normalized_total_instant_score = normalize(instantsTotalScore, min_instant_score, max_instant_score);
        float weight_profile_views = 0.4f;
        float weight_total_article_score = 0.4f;
        float weight_total_instant_score = 0.2f;

        return (
                weight_profile_views * normalized_profile_views +
                        weight_total_article_score * normalized_total_article_score +
                        weight_total_instant_score * normalized_total_instant_score

        );
    }


    public Float calculateInstantScore(Instant instant) {
        List<Instant> userInstants = instantRepository.getInstantsByUserId(instant.getUserId(), PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        int min_views = Integer.MAX_VALUE;
        int max_views = 0;
        int max_ratingSum = 0;
        int min_ratingSum = Integer.MAX_VALUE;
        int min_ratingNumbers = Integer.MAX_VALUE;
        int max_ratingNumbers = 0;

        for (Instant userInstant : userInstants) {
            if (userInstant.getViews() > max_views) {
                max_views = userInstant.getViews();
            }
            if (userInstant.getViews() < min_views) {
                min_views = userInstant.getViews();
            }
            if (userInstant.getRatingsNumber() > max_ratingNumbers) {
                max_ratingNumbers = userInstant.getRatingsNumber();
            }
            if (userInstant.getRatingsNumber() < min_ratingNumbers) {
                min_ratingNumbers = userInstant.getRatingsNumber();
            }
        }
        List<Rating> ratings = ratingRepository.findByPostIdAndPostType(instant.getId(), PostType.INSTANT, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        int usefulRatingCount = 0;
        int notUsefulRatingCount = 0;
        int min_usefulRatingCount = Integer.MAX_VALUE;
        int max_usefulRatingCount = 0;
        int min_notUsefulRatingCount = Integer.MAX_VALUE;
        int max_notUsefulRatingCount = 0;

        for (Rating rating : ratings) {
            usefulRatingCount += rating.getUsefulRating().size();
            notUsefulRatingCount += rating.getUsefulRating().size();
            if (rating.getUsefulRating().size() > max_usefulRatingCount) {
                max_usefulRatingCount = rating.getUsefulRating().size();
            }
            if (rating.getUsefulRating().size() < min_usefulRatingCount) {
                min_usefulRatingCount = rating.getUsefulRating().size();
            }
            if (rating.getNotUsefulRating().size() > max_notUsefulRatingCount) {
                max_notUsefulRatingCount = rating.getNotUsefulRating().size();
            }
            if (rating.getUsefulRating().size() < min_notUsefulRatingCount) {
                min_notUsefulRatingCount = rating.getNotUsefulRating().size();
            }
        }
        float normalized_views = normalize(instant.getViews(), min_views, max_views);
        float normalized_rating_sum = normalize(instant.getRatingSum(), min_ratingSum, max_ratingSum);
        float normalized_rating_numbers = normalize(instant.getRatingsNumber(), min_ratingNumbers, max_ratingNumbers);
        float normalized_useful_ratings = normalize(usefulRatingCount, min_usefulRatingCount, max_usefulRatingCount);
        float normalized_not_useful_ratings = normalize(notUsefulRatingCount, min_notUsefulRatingCount, max_notUsefulRatingCount);
        return (
                weight_views * normalized_views +
                        weight_rating_sum * normalized_rating_sum +
                        weight_rating_numbers * normalized_rating_numbers +
                        weight_useful_ratings * normalized_useful_ratings +
                        weight_not_useful_ratings * normalized_not_useful_ratings
        );
    }


    public float normalize(int value, int min_value, int max_value) {
        return (value - min_value) / (max_value - min_value);
    }

    public float normalize(float value, float min_value, float max_value) {
        return (value - min_value) / (max_value - min_value);
    }


}
