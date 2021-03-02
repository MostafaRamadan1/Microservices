package com.mircoserivces.ratingdataservice;

import com.mircoserivces.ratingdataservice.models.Rating;
import com.mircoserivces.ratingdataservice.models.Ratings;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RatingController {

    private static List<Rating> ratings = new ArrayList<Rating>(){{
        add(new Rating(1,1, 5));
        add(new Rating(1,2, 2));
        add(new Rating(1,3, 4));
        add(new Rating(2,4, 1));
        add(new Rating(2,5, 5));
        add(new Rating(2,6, 3));
    }};

    @RequestMapping("/ratings/{userId}")
    public Ratings getRating(@PathVariable int userId){
        List<Rating> filteredRatings =  ratings.stream().filter(r -> r.userId == userId).collect(Collectors.toList());
        return new Ratings(filteredRatings);
    }
}
