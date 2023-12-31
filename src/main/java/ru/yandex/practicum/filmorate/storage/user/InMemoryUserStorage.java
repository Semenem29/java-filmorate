package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements Storage<User> {
    private Map<Integer, User> userStorage = new HashMap<>();
    private int userId = 0;

    @Override
    public Map<Integer, User> getStorage() {
        return userStorage;
    }

    @Override
    public User create(User user) {
        validationCheck(user);
        if (user.getId() == null) {
            user.setId(generateId());
        }
        if (userStorage.containsKey(user.getId())) {
            String message = "error, the user already exists";
            log.error(message);
            throw new UserAlreadyExistException(message);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        userStorage.put(user.getId(), user);
        String message = String.format("Добавлен новый юзер, количество пользователей: %s", userStorage.size());
        log.info(message);
        return user;
    }

    @Override
    public User update(User user) {
        validationCheck(user);
        if (user.getName().isBlank() || user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (userStorage.containsKey(user.getId())) {
            userStorage.put(user.getId(), user);
            log.info("Обновление пользователя с id '{}' успешно произведена", user.getId());
        } else {
            String message = "error, the user doesn't exist";
            log.error(message);
            throw new UserNotExistException(message);
        }

        return user;
    }

    private int generateId() {
        return ++userId;
    }

    private void validationCheck(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String message = "bad email";
            log.error(message);
            throw new UserValidationException(message);
        }
        if (user.getLogin().equals("") || user.getLogin().contains(" ")) {
            String message = "bad login";
            log.error(message);
            throw new UserValidationException(message);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "bad birthday";
            log.error(message);
            throw new UserValidationException(message);
        }
    }
}
