package com.mircoserivces.ratingdataservice;

import com.mircoserivces.ratingdataservice.models.Rating;
import com.mircoserivces.ratingdataservice.models.Ratings;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RatingController {

    private static List<Rating> ratings = new ArrayList<Rating>() {{
        add(new Rating(1, 1, 5));
        add(new Rating(1, 2, 2));
        add(new Rating(1, 3, 4));
        add(new Rating(2, 4, 1));
        add(new Rating(2, 5, 5));
        add(new Rating(2, 6, 3));
    }};

    @GetMapping("/ratings/{userId}")
    public Mono<Ratings> getRating(@PathVariable int userId) {
        List<Rating> filteredRatings = ratings.stream().filter(r -> r.userId == userId).collect(Collectors.toList());
        return Mono.just(new Ratings(filteredRatings));
    }
}
