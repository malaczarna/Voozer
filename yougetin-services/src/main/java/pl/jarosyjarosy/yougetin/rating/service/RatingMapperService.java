package pl.jarosyjarosy.yougetin.rating.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.rating.endpoint.message.RatingMessage;
import pl.jarosyjarosy.yougetin.rating.model.Rating;

@Component
public class RatingMapperService {

    public RatingMessage mapRating(Rating rating) {
        RatingMessage ratingMessage = new RatingMessage();
        BeanUtils.copyProperties(rating, ratingMessage);

        return ratingMessage;
    }

    public Rating mapRatingMessage(RatingMessage ratingMessage) {
        Rating rating = new Rating();
        BeanUtils.copyProperties(ratingMessage, rating);

        return rating;
    }
}
