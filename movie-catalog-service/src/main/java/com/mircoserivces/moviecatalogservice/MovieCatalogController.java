package com.mircoserivces.moviecatalogservice;

import com.mircoserivces.moviecatalogservice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClient;

    @RequestMapping("/catalogs/{userId}")
    public Mono<Catalogs> getCatalogs(@PathVariable int userId) throws InterruptedException {
        List<Catalog> catalogList = new ArrayList<>();
        return getCatalogsMono(userId);
    }

    private Mono<Catalogs> getCatalogsMono(int userId) {

        return webClient
                .build()
                .get()
                .uri("http://rating-data-service/ratings/{userId}", userId)
                .retrieve()
                .bodyToMono(Ratings.class)
                .zipWhen(data -> getMovie(data.getRatings().stream().map(r -> r.getMovieId()).collect(Collectors.toList())))
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

    Mono<Movies> getMovie(List<Integer> movieIds) {

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        for (Integer movieId : movieIds) {
            queryParams.add("movieIds", movieId.toString());
        }

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://MOVIE-INFO-SERVICE");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return webClient
                .uriBuilderFactory(factory)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movies/")
                        .queryParams(queryParams)
                        .build()
                )
                .retrieve()
                .bodyToMono(Movies.class)
                ;
    }
}
