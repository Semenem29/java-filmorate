package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    public void addFriend(int userId, int friendId);

    public void deleteFriend(int userId, int friendId);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);
}
