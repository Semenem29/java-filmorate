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
    private Integer rate;

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

