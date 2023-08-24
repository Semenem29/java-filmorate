package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class FilmService {
    private FilmStorage filmStorage;
    private UserService userService;
    private UserStorage userStorage;

    private static final int NUMBER_OF_FILMS_IN_TOP = 10;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.userStorage = this.userService.getUserStorage();
    }

    public void addLike(Integer filmId, Integer userId){
        if (filmId == null || userId == null) {
            return;
        }

        Film film = filmStorage.getFilmStorage().get(filmId);
        User user = userStorage.getUsers().get(userId);
        if (user == null || film == null) {
            return;
        }

        film.getFans().add(userId);
    }

    public void removeLike(Integer filmId, Integer userId){
        if (filmId == null || userId == null) {
            return;
        }

        Film film = filmStorage.getFilmStorage().get(filmId);
        User user = userStorage.getUsers().get(userId);
        if (user == null || film == null) {
            return;
        }

        film.getFans().remove(userId);
    }

    public List<Film> getTop(){
        return filmStorage.getFilmStorage().stream()
                .sorted(Film::compare)
                .limit(NUMBER_OF_FILMS_IN_TOP)
                .collect(Collectors.toList());
    }
}
