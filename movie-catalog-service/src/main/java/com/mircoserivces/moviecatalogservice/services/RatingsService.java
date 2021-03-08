package com.mircoserivces.moviecatalogservice.services;

import com.mircoserivces.moviecatalogservice.models.Movie;
import com.mircoserivces.moviecatalogservice.models.Movies;
import com.mircoserivces.moviecatalogservice.models.Rating;
import com.mircoserivces.moviecatalogservice.models.Ratings;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class RatingsService {

    @Autowired
    private WebClient.Builder webClient;

    // All hystrix configurations: https://github.com/Netflix/Hystrix/wiki/Configuration
    // Configure hystrix as Circuit breaker
    @HystrixCommand(fallbackMethod = "getRatingsFallback", commandProperties = {
            // if the the service does not get a response for 2 sec, enable the circuit breaker
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
            // maximum percentage of error that is allowed in a request volume of '20' requests,
            // if this happened fire the circuit breaker for 5 sec, and then switch it off again and continue receiving requests
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="20"),
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="20"),
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="5000"),
    })
    public Mono<Ratings> getRatings(int userId){

        return webClient
                .build()
                .get()
                .uri("http://rating-data-service/ratings/{userId}", userId)
                .retrieve()
                .bodyToMono(Ratings.class);
    }

    public Mono<Ratings> getRatingsFallback(int userId) {
        Ratings ratings = new Ratings(Arrays.asList(new Rating(0, 0, 0)));
        return Mono.just(ratings);
    }
}
