package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageTest {
    private final FriendStorage storage;

    @Test
    void shouldAddDeleteGetFriend(@Qualifier("userDbStorage") UserStorage userStorage) {
        User user1 = new User(1, "mur@email.com", "mur",
                "murka", LocalDate.parse("2005-05-26"), new HashSet<>());
        User user2 = new User(2, "film@email.com", "film",
                "Tom", LocalDate.parse("1988-08-17"), new HashSet<>());
        userStorage.create(user1);
        userStorage.create(user2);
        storage.addFriend(user1.getId(), user2.getId());
        List<User> friends = storage.getFriends(user1.getId());

        assertEquals(1, friends.size());
        assertEquals(user2, friends.get(0));
    }

    @Test
    void shouldGetCommonFriends(@Qualifier("userDbStorage") UserStorage userStorage) {
        User user1 = new User(1, "mur@email.com", "mur",
                "murka", LocalDate.parse("2005-05-26"), new HashSet<>());
        User user2 = new User(2, "film@email.com", "film",
                "Tom", LocalDate.parse("1988-08-17"), new HashSet<>());
        User user3 = new User(3, "maria@email.com", "Masha",
                "Maria", LocalDate.parse("1998-08-17"), new HashSet<>());
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        storage.addFriend(user1.getId(), user2.getId());
        storage.addFriend(user1.getId(), user3.getId());
        storage.addFriend(user2.getId(), user3.getId());
        List<User> commonFriends = storage.getCommonFriends(user1.getId(), user2.getId());

        assertEquals(1, commonFriends.size());
        assertEquals(user3, commonFriends.get(0));
    }
}
