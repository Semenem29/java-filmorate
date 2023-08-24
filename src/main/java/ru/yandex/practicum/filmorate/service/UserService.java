package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null || userId.equals(friendId)) {
            return;
        }

        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            return;
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null || userId.equals(friendId)) {
            return;
        }

        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            return;
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        if (userId == null || friendId == null || userId.equals(friendId)) {
            return null;
        }

        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            return null;
        }

        return user.getFriends().stream()
                .filter(id -> friend.getFriends().contains(id))
                .map(id -> userStorage.getUsers().get(id))
                .collect(Collectors.toList());
    }
}
