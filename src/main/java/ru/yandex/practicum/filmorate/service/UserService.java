package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.service.validator.UserValidator.validate;

@Service
public class UserService {

    private UserStorage userStorage;

    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        friendStorage.addFriend(id, friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        friendStorage.deleteFriend(id, friendId);
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public List<User> getCommonFriends(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        return friendStorage.getCommonFriends(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        getUserById(id);
        return friendStorage.getFriends(id);
    }

    public List<User> getUsers() {
        return userStorage.getStorage();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }
}
