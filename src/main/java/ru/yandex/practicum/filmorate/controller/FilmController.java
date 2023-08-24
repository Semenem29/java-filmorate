package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
        return new ArrayList<>(filmService.getFilmStorage().values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Set<Integer> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Set<Integer> removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable(required = false) Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = NUMBER_OF_FILMS_IN_TOP_BY_DEFAULT) Integer count) {
        if (count <= 0) {
            count = Integer.parseInt(NUMBER_OF_FILMS_IN_TOP_BY_DEFAULT);
        }
        return filmService.getTop(count);
    }
}
