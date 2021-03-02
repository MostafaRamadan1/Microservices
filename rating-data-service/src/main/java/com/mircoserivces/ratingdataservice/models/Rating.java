package com.mircoserivces.ratingdataservice.models;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public int userId;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public int movieId;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public int userRating;

}
