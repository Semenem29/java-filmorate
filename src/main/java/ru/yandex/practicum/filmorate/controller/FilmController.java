package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> filmStorage = new HashMap<>();
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private int filmId = 0;

    @GetMapping
    public List<Film> getFilmStorage(){
        return new ArrayList<>(filmStorage.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film){
        validationCheck(film);
        if (filmStorage.containsKey(film.getId())){
            String message = "error, the film already exists";
            log.error(message);
            throw new FilmAlreadyExistException(message);
        }

        film.setId(++filmId);
        filmStorage.put(film.getId(), film);
        String message = String.format("Добавлен новый фильм, количество фильмов в хранилище %s", filmStorage.size());
        log.info(message);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film){
        validationCheck(film);
        if (filmStorage.containsKey(film.getId())){
            filmStorage.put(film.getId(), film);
            log.info("Обновление фильма с id '{}' успешно произведена", film.getId());
        } else {
            String message = "error, the film doesn't exist";
            log.error(message);
            throw new FilmNotExistException(message);
        }

        return film;
    }

    private void validationCheck(Film film){
        if (film == null){
            String message = "error, null was provided";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getName() == null || film.getName().equals("")){
            String message = "bad name was provided";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getDescription().length() > 200){
            String message = "too long description. it should be less or equals 200 letters";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)){
            String message = "bad release date";
            log.error(message);
            throw new FilmValidationException(message);
        }
        if (film.getDuration() <= 0){
            String message = "bad duration";
            log.error(message);
            throw new FilmValidationException(message);
        }
    }
}
