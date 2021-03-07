package com.mircoserivces.movieinfoservice;

import com.mircoserivces.movieinfoservice.models.Movie;
import com.mircoserivces.movieinfoservice.models.Movies;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieController {

    private static List<Movie> moviesDataSource = new ArrayList<Movie>() {{
        add(new Movie(1, "Titanic", "Romantic drama movie"));
        add(new Movie(2, "Transporter", "Action movie"));
        add(new Movie(3, "Extractor", "Action movie"));
        add(new Movie(4, "Dump and Dump", "Comedy movie"));
        add(new Movie(5, "Lord of rings", "Imagination and magic movie"));
        add(new Movie(6, "Harry potter", "Imagination and magic movie"));
    }};

    @RequestMapping(value = "/movies", params = "movieIds")
    public Mono<Movies> getMovie(@RequestParam List<Integer> movieIds) {
        List<Movie> movies = moviesDataSource.stream().filter(m -> movieIds.contains(m.movieId)).collect(Collectors.toList());
        return Mono.just(new Movies(movies));
    }

    @RequestMapping("/isMovieLive")
    public String isMovieLive() {
        return "Aywa";
    }
}
