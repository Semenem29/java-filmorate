package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Test
    void shouldCreate() {
        Film film1 = new Film(null, "Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(1, "G"), new ArrayList<>());
        Film storagedFilm = filmStorage.create(film1);
        assertEquals(film1, storagedFilm);

        Film film2 = new Film(null, "Die Hard 2",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level! Again!",
                LocalDate.of(1990, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(2, "PG"), new ArrayList<>());
        filmStorage.create(film2);

        List<Film> films = filmStorage.getStorage();
        assertEquals(2, films.size());
        assertEquals(film1, films.get(0));
        assertEquals(film2, films.get(1));
    }

    @Test
    void shouldUpdate() {
        Film film = new Film(null, "Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film);
        Film newFilm = new Film(null, "Die Hard 2",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level! Again!",
                LocalDate.of(1990, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(2, "PG"), new ArrayList<>());
        filmStorage.create(newFilm);
        newFilm.setId(film.getId());
        Film updatedFilm = filmStorage.update(newFilm);

        assertEquals(newFilm, updatedFilm);
    }

    @Test
    void shouldNotUpdateWhenIncorrectId() {
        FilmNotExistException e = Assertions.assertThrows(
                FilmNotExistException.class,
                () -> {
                    Film film = new Film(null, "Die Hard",
                            "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                            LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                            0, new RatingMPA(1, "G"), new ArrayList<>());
                    film.setId(9999);
                    filmStorage.update(film);
                }
        );

        assertEquals("Film with id=9999 wasn't found", e.getMessage());
    }

    @Test
    void shouldGetFilmById() {
        Film film = new Film(null, "Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film);

        Film filmById = filmStorage.getFilmById(film.getId());
        assertEquals(film, filmById);
    }

    @Test
    void shouldNotGetFilmWhenIncorrectId() {
        FilmNotExistException e = Assertions.assertThrows(
                FilmNotExistException.class,
                () -> filmStorage.getFilmById(9999)
        );

        assertEquals("Film with id=9999 wasn't found", e.getMessage());
    }

    @Test
    void shouldGetTopTenFilms(@Autowired LikeStorage likeStorage) {
        Film film1 = new Film(null, "Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film1);
        Film film2 = new Film(null, "Die Hard 2",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level! Again!",
                LocalDate.of(1990, 1, 1), 133, new HashSet<>(),
                0, new RatingMPA(2, "PG"), new ArrayList<>());
        filmStorage.create(film2);
        User user = new User(1, "zachbraff@scrubs.com", "jd29",
                "John", LocalDate.of(1975, 4, 6), new HashSet<>());
        userStorage.create(user);
        likeStorage.addLike(film2.getId(), user.getId());
        List<Film> mostPopular = filmStorage.getTop(10);

        assertEquals(film2, mostPopular.get(0));
        assertEquals(film1, mostPopular.get(1));
    }
}
