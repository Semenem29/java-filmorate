package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTest {
    private final LikeStorage storage;

    @Test
    void shouldAddLikes(@Qualifier("userDbStorage") UserStorage userStorage,
                        @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        Film film = new Film(null, "Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                null, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film);
        User user = new User(1, "zachbraff@scrubs.com", "jd29",
                "John", LocalDate.of(1975, 4, 6), new HashSet<>());
        User user2 = new User(2, "z2achbraff@scrubs.com", "jd292",
                "John2", LocalDate.of(1975, 4, 6), new HashSet<>());
        userStorage.create(user);
        userStorage.create(user2);
        storage.addLike(film.getId(), user.getId());
        storage.addLike(film.getId(), user2.getId());
        List<Integer> likes = storage.getFans(film.getId());

        assertEquals(List.of(user.getId(), user2.getId()), likes);
    }

    @Test
    void shouldDeleteLikes(@Qualifier("userDbStorage") UserStorage userStorage,
                           @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        Film film = new Film(null, "Die Hard",
                "40 Storeys High - with Suspense, Excitement and Adventure on every level!",
                LocalDate.of(1988, 1, 1), 133, new HashSet<>(),
                null, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film);
        User user = new User(1, "zachbraff@scrubs.com", "jd29",
                "John", LocalDate.of(1975, 4, 6), new HashSet<>());
        userStorage.create(user);
        storage.addLike(film.getId(), user.getId());
        List<Integer> likes = storage.getFans(film.getId());
        assertEquals(List.of(user.getId()), likes);
        storage.deleteLike(film.getId(), user.getId());
        likes = storage.getFans(film.getId());

        assertTrue(likes.isEmpty());
    }
}
