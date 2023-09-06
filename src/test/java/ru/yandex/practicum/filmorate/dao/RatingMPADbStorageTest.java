package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.RatingMPANotExistException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.mpa.RatingMPAStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMPADbStorageTest {
    private final RatingMPAStorage storage;

    @Test
    void shouldGetAll() {
        List<RatingMPA> ratingMPA = storage.getAll();
        List<RatingMPA> testList = List.of(
                new RatingMPA(1, "G"),
                new RatingMPA(2, "PG"),
                new RatingMPA(3, "PG-13"),
                new RatingMPA(4, "R"),
                new RatingMPA(5, "NC-17")
        );

        assertEquals(testList, ratingMPA);
    }

    @Test
    void shouldGetRatingMPAById() {
        RatingMPA ratingMPA = storage.getRatingMPAById(1);
        assertEquals(new RatingMPA(1, "G"), ratingMPA);

        ratingMPA = storage.getRatingMPAById(2);
        assertEquals(new RatingMPA(2, "PG"), ratingMPA);

        ratingMPA = storage.getRatingMPAById(3);
        assertEquals(new RatingMPA(3, "PG-13"), ratingMPA);

        ratingMPA = storage.getRatingMPAById(4);
        assertEquals(new RatingMPA(4, "R"), ratingMPA);

        ratingMPA = storage.getRatingMPAById(5);
        assertEquals(new RatingMPA(5, "NC-17"), ratingMPA);
    }

    @Test
    void shouldNotGetRatingMPAWhenIncorrectId() {
        RatingMPANotExistException e = Assertions.assertThrows(
                RatingMPANotExistException.class,
                () -> storage.getRatingMPAById(9999)
        );

        assertEquals("MPA with id=9999 wasn't found", e.getMessage());
    }
}
