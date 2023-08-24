package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> fans;
    private int rate;

    public static int compare(Film f1, Film f2){
        return Integer.compare(f1.getFans().size(), f2.getFans().size());
    }
}
