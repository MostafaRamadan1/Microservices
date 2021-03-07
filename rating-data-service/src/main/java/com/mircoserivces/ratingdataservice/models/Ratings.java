package com.mircoserivces.ratingdataservice.models;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Ratings {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    List<Rating> ratings;
}
