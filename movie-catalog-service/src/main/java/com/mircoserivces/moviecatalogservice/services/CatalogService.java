package com.mircoserivces.moviecatalogservice.services;

import com.mircoserivces.moviecatalogservice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
@Service
public class CatalogService {

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private MoviesService moviesService;

    @Autowired
    private RatingsService ratingsService;

    public Mono<Catalogs> getCatalogsMono(int userId) {

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
