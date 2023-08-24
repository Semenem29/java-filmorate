package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    public Map<Integer, Film> getFilmStorage();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);
}
