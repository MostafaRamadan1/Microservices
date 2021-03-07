package com.mircoserivces.moviecatalogservice.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Catalogs {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    List<Catalog> catalogs = new ArrayList<>();
}
