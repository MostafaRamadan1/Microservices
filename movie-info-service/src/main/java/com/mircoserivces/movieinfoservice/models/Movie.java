package com.mircoserivces.movieinfoservice.models;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    public int movieId;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public String name;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public String description;
}
