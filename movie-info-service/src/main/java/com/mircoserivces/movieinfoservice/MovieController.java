package com.mircoserivces.movieinfoservice;

import com.mircoserivces.movieinfoservice.models.Movie;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    private static List<Movie> movies = new ArrayList<Movie>(){{
        add(new Movie(1, "Titanic", "Romantic drama movie"));
        add(new Movie(2, "Transporter", "Action movie"));
        add(new Movie(3, "Extractor", "Action movie"));
        add(new Movie(4, "Dump and Dump", "Comedy movie"));
        add(new Movie(5, "Lord of rings", "Imagination and magic movie"));
        add(new Movie(6, "Harry potter", "Imagination and magic movie"));
    }};

    @RequestMapping("/movies/{movieId}")
    public Movie getMovie(@PathVariable int movieId){
        for (Movie m: movies) {
            if(m.movieId == movieId){
                return m;
            }
        }
        return null;
    }
}
