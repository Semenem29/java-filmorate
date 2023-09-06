package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> getStorage();

    public Film create(Film film);

    public Film update(Film film);

    public Film getFilmById(int id);

    public List<Film> getTop(int count);
}
