package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> fans;
    private Integer rate;
    private RatingMPA mpa;
    private List<Genre> genres;

    public static int compare(Film f1, Film f2) {
        if (f2.getRate() != null) {
            if (f1.getRate() != null) {
                if (!f2.getRate().equals(f1.getRate())) {
                    return f2.getRate() - f1.getRate();
                }
            } else {
                return 1;
            }
        } else if (f1.getRate() != null) {
            return -1;
        }
        return f2.getId() - f1.getId();
    }
}

