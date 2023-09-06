package ru.yandex.practicum.filmorate.service.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public static void validate(User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String message = "bad email!";
            log.error(message);
            throw new UserValidationException(message);
        }
        if (user.getLogin().equals("") || user.getLogin().contains(" ")) {
            String message = "bad login!";
            log.error(message);
            throw new UserValidationException(message);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "bad birthday!";
            log.error(message);
            throw new UserValidationException(message);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}



