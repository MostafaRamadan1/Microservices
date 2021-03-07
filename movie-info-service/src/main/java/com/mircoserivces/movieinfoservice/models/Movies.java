package com.mircoserivces.movieinfoservice.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Movies {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    List<Movie> movies;
}
