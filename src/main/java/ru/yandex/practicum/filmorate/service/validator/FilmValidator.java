package ru.yandex.practicum.filmorate.service.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    public static void validate(Film film) {
        if (film == null) {
            String message = "error, null was provided!";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getName() == null || film.getName().equals("") || film.getName().isBlank()) {
            String message = "bad name was provided!";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getDescription().length() > 200) {
            String message = "too long description. it should be less or equals 200 letters!";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            String message = "bad release date!";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getDuration() <= 0) {
            String message = "bad duration!";
            log.error(message);
            throw new FilmValidationException(message);
        }
    }
}
