package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> userStorage = new HashMap<>();
    private int userId = 1;

    @GetMapping
    public List<User> getUsers(){
        return new ArrayList<>(userStorage.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        validationCheck(user);
        if (user.getId() == null){
            user.setId(userId++);
        }
        if (userStorage.containsKey(user.getId())){
            String message = "error, the user already exists";
            log.error(message);
            throw new UserAlreadyExistException(message);
        }

       if (user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }

        userStorage.put(user.getId(), user);
        String message = String.format("Добавлен новый юзер, количество пользователей: %s", userStorage.size());
        log.info(message);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user){
        validationCheck(user);
        if (user.getName().isBlank() || user.getName() == null || user.getName().equals("")){
            user.setName(user.getLogin());
        }
        if (userStorage.containsKey(user.getId())){
            userStorage.put(user.getId(), user);
            log.info("Обновление пользователя с id '{}' успешно произведена", user.getId());
        } else {
            String message = "error, the user doesn't exist";
            log.error(message);
            throw new UserNotExistException(message);
        }

        return user;
    }

    private void validationCheck(User user){
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")){
            String message = "bad email";
            log.error(message);
            throw new UserValidationException(message);
        }
        if (user.getLogin().equals("") || user.getLogin().contains(" ")){
            String message = "bad login";
            log.error(message);
            throw  new UserValidationException(message);
        }
        if (user.getBirthday().isAfter(LocalDate.now())){
            String message = "bad birthday";
            log.error(message);
            throw new UserValidationException(message);
        }
    }
}
