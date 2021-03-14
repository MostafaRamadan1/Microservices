package com.mircoserivces.moviecatalogservice.services;

import com.mircoserivces.moviecatalogservice.models.Movie;
import com.mircoserivces.moviecatalogservice.models.Movies;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class MoviesService {

    @Autowired
    private WebClient.Builder webClient;

    @Value("${movie-service-url}")
    private String movieInfoServiceUrl;

    // All hystrix configurations: https://github.com/Netflix/Hystrix/wiki/Configuration
    // Configure hystrix as Bulkhead via threads configuration
    @HystrixCommand(fallbackMethod = "getMoviesFallback", threadPoolKey = "moviesThtreadPool",
            threadPoolProperties = {
            // maximum allowed threads that can access this api is 20, if exceeded but it in the queue which size is 10 \
            // if it goes beyond this, call the fallback method
            @HystrixProperty(name="coreSize", value="20"),
            @HystrixProperty(name="maxQueueSize", value="10"),
    })

    public Mono<Movies> getMovies(List<Integer> movieIds) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        for (Integer movieId : movieIds) {
            queryParams.add("movieIds", movieId.toString());
        }

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(movieInfoServiceUrl);
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

    public Mono<Movies> getMoviesFallback(List<Integer> movieIds) {
        Movies movies = new Movies(Arrays.asList(new Movie(0, "No movies available right now", "No movies available right now")));
        return Mono.just(movies);
    }

}
