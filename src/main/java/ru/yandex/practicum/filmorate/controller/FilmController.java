package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    private static final String NUMBER_OF_FILMS_IN_TOP_BY_DEFAULT = "10";

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilmStorage() {
        return new ArrayList<>(filmService.getFilms());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable(required = false) Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = NUMBER_OF_FILMS_IN_TOP_BY_DEFAULT) Integer count) {
        return filmService.getTop(count);
    }
}
