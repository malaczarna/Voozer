package pl.jarosyjarosy.yougetin.rating.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.rating.endpoint.message.RatingMessage;
import pl.jarosyjarosy.yougetin.rating.model.RatingSearchType;
import pl.jarosyjarosy.yougetin.rating.service.RatingMapperService;
import pl.jarosyjarosy.yougetin.rating.service.RatingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    private final RatingService ratingService;
    private final RatingMapperService ratingMapperService;

    @Autowired
    public RatingController(RatingService ratingService,
                            RatingMapperService ratingMapperService) {
        this.ratingService = ratingService;
        this.ratingMapperService = ratingMapperService;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public RatingMessage getUserRatings(@PathVariable Long id, HttpServletRequest request) {

        return ratingMapperService.mapRating(ratingService.get(id));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public RatingMessage saveRating(@RequestBody RatingMessage ratingMessage, HttpServletRequest request) {

        return ratingMapperService.mapRating(ratingService.validateAndCreate(ratingMapperService.mapRatingMessage(ratingMessage), new Identity(request).getUserId()));
    }

    @RequestMapping(
            value = "/search-command",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public List<RatingMessage> getUserRatings(@RequestBody String type, HttpServletRequest request) {

        return ratingService.search(new Identity(request).getUserId(), RatingSearchType.valueOf(type)).stream()
                .map(ratingMapperService::mapRating).collect(Collectors.toList());
    }
}
