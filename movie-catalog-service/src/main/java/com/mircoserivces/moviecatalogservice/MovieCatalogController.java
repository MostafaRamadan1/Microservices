package com.mircoserivces.moviecatalogservice;

import com.mircoserivces.moviecatalogservice.models.*;
import com.mircoserivces.moviecatalogservice.services.CatalogService;
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
    CatalogService catalogService;

    @RequestMapping("/catalogs/{userId}")
    public Mono<Catalogs> getCatalogs(@PathVariable int userId) {
        return catalogService.getCatalogsMono(userId);
    }
}
