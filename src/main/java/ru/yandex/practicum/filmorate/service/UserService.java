package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private Storage<User> userStorage;

    @Autowired
    public UserService(Storage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public Set<Integer> addFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            throw new UserValidationException("friendId and/or userId cannot be null");
        }

        if (userId.equals(friendId)) {
            throw new UserValidationException("same ids was provided");
        }

        Map<Integer, User> fetchedUsers = getUsers();
        User user = fetchedUsers.get(userId);
        User friend = fetchedUsers.get(friendId);
        if (user == null || friend == null) {
            throw new UserNotExistException("user or friend was not found");
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        return user.getFriends();
    }

    public Set<Integer> removeFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            throw new UserValidationException("friendId and/or userId cannot be null");
        }

        if (userId.equals(friendId)) {
            throw new UserValidationException("same ids was provided");
        }

        Map<Integer, User> fetchedUsers = getUsers();
        User user = fetchedUsers.get(userId);
        User friend = fetchedUsers.get(friendId);
        if (user == null || friend == null) {
            throw new UserNotExistException("user or friend was not found");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        return user.getFriends();
    }

    public Set<User> getCommonFriends(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            throw new UserValidationException("friendId and/or userId cannot be null");
        }

        if (userId.equals(friendId)) {
            throw new UserValidationException("same ids was provided");
        }

        Map<Integer, User> fetchedUsers = getUsers();
        User user = fetchedUsers.get(userId);
        User friend = fetchedUsers.get(friendId);
        if (user == null || friend == null) {
            throw new UserNotExistException("user or friend was not found");
        }

        if (user.getFriends() == null) {
            return new HashSet<>();
        } else {
            return user.getFriends().stream()
                    .filter(id -> friend.getFriends().contains(id))
                    .map(id -> getUsers().get(id))
                    .collect(Collectors.toSet());
        }

    }

    public List<User> getFriends(Integer userId) {
        if (userId == null) {
            throw new UserNotExistException("userId can not be null");
        }

        User user = getUsers().get(userId);
        if (user == null) {
            throw new UserNotExistException("user was not found");
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        return user.getFriends().stream()
                .map(id -> getUsers().get(id))
                .collect(Collectors.toList());
    }

    public User getUser(Integer userId) {
        if (userId == null) {
            throw new UserNotExistException("userId can not be null");
        }
        if (userId <= 0 || getUsers().containsKey(userId)) {
            return getUsers().get(userId);
        } else {
            throw new UserNotExistException("user is not exist");
        }

    }

    public Map<Integer, User> getUsers() {
        return userStorage.getStorage();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }
}
