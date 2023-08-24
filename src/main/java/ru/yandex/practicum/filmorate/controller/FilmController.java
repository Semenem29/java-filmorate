package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    private FilmStorage filmStorage;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
        this.filmStorage = filmService.getFilmStorage();
    }

    @Autowired


    @GetMapping
    public List<Film> getFilmStorage() {
        return filmStorage.getFilmStorage();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }


}
