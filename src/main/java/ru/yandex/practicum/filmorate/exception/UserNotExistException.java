package ru.yandex.practicum.filmorate.exception;

public class UserNotExistException extends NotExistException {

    public UserNotExistException(String message) {
        super(message);
    }
}
