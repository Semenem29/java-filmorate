package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.GenreNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreStorage storage;

    @Test
    void shouldGetAll() {
        List<Genre> genres = storage.getAll();
        List<Genre> testList = List.of(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик")
        );

        assertEquals(testList, genres);
    }

    @Test
    void shouldGetGenreById() {
        Genre genre = storage.getGenreById(1);
        assertEquals(new Genre(1, "Комедия"), genre);

        genre = storage.getGenreById(2);
        assertEquals(new Genre(2, "Драма"), genre);

        genre = storage.getGenreById(3);
        assertEquals(new Genre(3, "Мультфильм"), genre);

        genre = storage.getGenreById(4);
        assertEquals(new Genre(4, "Триллер"), genre);

        genre = storage.getGenreById(5);
        assertEquals(new Genre(5, "Документальный"), genre);

        genre = storage.getGenreById(6);
        assertEquals(new Genre(6, "Боевик"), genre);
    }

    @Test
    void shouldNotGetGenreWhenIncorrectId() {
        GenreNotExistException e = Assertions.assertThrows(
                GenreNotExistException.class,
                () -> storage.getGenreById(9999)
        );

        assertEquals("Genre was not found with id=9999", e.getMessage());
    }

    @Test
    void shouldAddDeleteFilmGenres(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        Film film = new Film("Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new RatingMPA(1, "G"));
        film.setGenres(List.of(
                new Genre(1, null),
                new Genre(4, null),
                new Genre(6, null)
        ));
        filmStorage.create(film);
        storage.addFilmGenres(film);
        List<Genre> genres = storage.getFilmGenres(film.getId());

        assertEquals(
                List.of(
                        new Genre(1, "Комедия"),
                        new Genre(4, "Триллер"),
                        new Genre(6, "Боевик")
                ),
                genres
        );

        storage.deleteFilmGenres(film.getId());
        genres = storage.getFilmGenres(film.getId());

        assertTrue(genres.isEmpty());
    }
}
