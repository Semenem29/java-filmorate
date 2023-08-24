package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Map<Integer, Film> getFilmStorage() {
        return filmStorage.getFilmStorage();
    }

    public Set<Integer> addLike(Integer filmId, Integer userId) {
        if (filmId == null || userId == null) {
            throw new NullPointerException();
        }

        Film film = filmStorage.getFilmStorage().get(filmId);
        if (film == null) {
            throw new FilmNotExistException("film is not exist");
        }

        User user = userStorage.getUsers().get(userId);
        if (user == null) {
            throw new UserNotExistException("user is not exist");
        }

        if (film.getFans() == null) {
            film.setFans(new HashSet<>());
        }

        if (film.getFans().add(userId)) {
            film.getFans().add(userId);
            if (film.getRate() == null) {
                film.setRate(1);
            } else {
                film.setRate(film.getRate() + 1);
            }
        }
        return film.getFans();
    }

    public Set<Integer> removeLike(Integer filmId, Integer userId) {
        if (filmId == null || userId == null) {
            throw new NullPointerException();
        }

        Film film = filmStorage.getFilmStorage().get(filmId);
        if (film == null) {
            throw new FilmNotExistException("film is not exist");
        }

        User user = userStorage.getUsers().get(userId);
        if (user == null) {
            throw new UserNotExistException("user is not exist");
        }

        if (film.getFans().remove(userId)) {
            film.getFans().remove(userId);
            film.setRate(film.getRate() - 1);
        }
        return film.getFans();
    }

    public List<Film> getTop(Integer count) {
        return filmStorage.getFilmStorage().values().stream()
                .sorted(Film::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(Integer id) {
        if (id == null) {
            throw new NullPointerException();
        }

        if (id <= 0 || getFilmStorage().containsKey(id)) {
            return getFilmStorage().get(id);
        } else {
            throw new FilmNotExistException("film is not exist");
        }

    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
}

