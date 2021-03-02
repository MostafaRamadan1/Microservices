package com.mircoserivces.moviecatalogservice.models;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Catalog {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public int movieId;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public String description;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public int userRating;
}
