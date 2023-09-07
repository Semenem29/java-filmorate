package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    @Qualifier("userDbStorage")
    private final UserStorage storage;

    @Test
    void shouldCreateAndGet() {
        User user1 = new User(1, "john666@gyandex.ru", "John666",
                "John", LocalDate.of(1966, 6, 6), new HashSet<>());
        User storagedUser = storage.create(user1);
        assertEquals(user1, storagedUser);

        User user2 = new User(2, "mike777@gyandex.ru", "Mike777",
                "Mike", LocalDate.of(1977, 7, 7), new HashSet<>());
        storage.create(user2);
        List<User> users = storage.getStorage();

        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }

    @Test
    void shouldUpdate() {
        User user = new User(1, "john666@gyandex.ru", "John666",
                "John", LocalDate.of(1966, 6, 6), new HashSet<>());
        storage.create(user);
        User newUser = new User(1, "mike777@gyandex.ru", "Mike777",
                "Mike", LocalDate.of(1977, 7, 7), new HashSet<>());
        User updatedUser = storage.update(newUser);

        assertEquals(newUser, updatedUser);
    }

    @Test
    void shouldNotUpdateWhenIncorrectId() {
        UserNotExistException e = Assertions.assertThrows(
                UserNotExistException.class,
                () -> {
                    User user = new User(777, "mike777@gyandex.ru", "Mike777",
                            "Mike", LocalDate.of(1977, 7, 7), new HashSet<>());
                    storage.update(user);
                }
        );

        assertEquals("User with id=777 was not found", e.getMessage());
    }

    @Test
    void shouldGetUserById() {
        User user = new User(1, "john666@gyandex.ru", "John666",
                "John", LocalDate.of(1966, 6, 6), new HashSet<>());
        User addedUser = storage.create(user);

        User userById = storage.getUserById(user.getId());
        assertEquals(user, userById);
    }

    @Test
    void shouldNotGetUserWhenIncorrectId() {
        UserNotExistException e = Assertions.assertThrows(
                UserNotExistException.class,
                () -> storage.getUserById(777)
        );

        assertEquals("User with id=777 was not found", e.getMessage());
    }
}
