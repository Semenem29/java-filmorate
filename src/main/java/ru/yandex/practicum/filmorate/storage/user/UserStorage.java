package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    public User createUser(User user);

    public User updateUser(User user);

    Map<Integer, User> getUsers();
}
