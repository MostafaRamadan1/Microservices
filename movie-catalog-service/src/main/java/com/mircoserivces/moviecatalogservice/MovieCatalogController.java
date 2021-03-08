package com.mircoserivces.moviecatalogservice;

import com.mircoserivces.moviecatalogservice.models.*;
import com.mircoserivces.moviecatalogservice.services.MoviesService;
import com.mircoserivces.moviecatalogservice.services.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private MoviesService moviesService;

    @Autowired
    private RatingsService ratingsService;

    @RequestMapping("/catalogs/{userId}")
    public Mono<Catalogs> getCatalogs(@PathVariable int userId) throws InterruptedException {
        List<Catalog> catalogList = new ArrayList<>();
        return getCatalogsMono(userId);
    }

    private Mono<Catalogs> getCatalogsMono(int userId) {

        return ratingsService.getRatings(userId)
                .zipWhen(data -> moviesService.getMovies(data.getRatings().stream().map(r -> r.getMovieId()).collect(Collectors.toList())))
                .flatMap(response -> {
                    Catalogs catalogs = new Catalogs();
                    Ratings ratings = response.getT1();
                    Movies movies = response.getT2();
                    ratings.getRatings().forEach(rating -> {
                        Movie movie = movies.getMovies().stream().filter(m -> m.movieId == rating.movieId).findFirst().get();
                        String movieDesc = movie == null ? "No desc available" : movie.description;
                        Catalog catalog = new Catalog(rating.movieId, movieDesc, rating.userRating);
                        catalogs.getCatalogs().add(catalog);
                    });
                    return Mono.just(catalogs);
                });
    }



}
