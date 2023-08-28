package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private Storage<Film> filmStorage;
    private Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> filmStorage, Storage<User> userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Map<Integer, Film> getFilmStorage() {
        return filmStorage.getStorage();
    }

    public Set<Integer> addLike(Integer filmId, Integer userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("filmId and/or userId cannot be null");
        }

        Film film = filmStorage.getStorage().get(filmId);
        if (film == null) {
            throw new FilmNotExistException("film is not exist");
        }

        User user = userStorage.getStorage().get(userId);
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
            throw new ValidationException("filmId and/or userId cannot be null");
        }

        Film film = filmStorage.getStorage().get(filmId);
        if (film == null) {
            throw new FilmNotExistException("film is not exist");
        }

        User user = userStorage.getStorage().get(userId);
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
        if (count <= 0) {
            throw new FilmValidationException("count of film cannot be less or equal to zero");
        }
        return filmStorage.getStorage().values().stream()
                .sorted(Film::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(Integer id) {
        if (id == null) {
            throw new FilmValidationException("filmId cannot be null");
        }

        if (id <= 0 || getFilmStorage().containsKey(id)) {
            return getFilmStorage().get(id);
        } else {
            throw new FilmNotExistException("film is not exist");
        }

    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }
}

