package com.mircoserivces.moviecatalogservice;

import com.mircoserivces.moviecatalogservice.models.*;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/catalogs/{userId}")
    public Catalogs getCatalogs(@PathVariable int userId){
        List<Catalog> catalogList = new ArrayList<>();
        Ratings ratings = restTemplate.getForObject("http://RATING-DATA-SERVICE/ratings/"+userId, Ratings.class);
        for (Rating r : ratings.getRatings()) {
            Movie m = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/"+r.getMovieId(), Movie.class);
            catalogList.add(new Catalog(m.movieId, m.getDescription(), r.userRating));
        }
        return new Catalogs(catalogList);
    }
}
