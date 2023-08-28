package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {
    private UserController controller;

    @BeforeEach
    public void createController() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void createUser() {
        assertEquals(0, controller.getUsers().size());
        User user = User.builder()
                .name("John")
                .email("john666@gyandex.ru")
                .login("John666")
                .birthday(LocalDate.of(1966, 6, 6))
                .build();
        controller.createUser(user);
        assertEquals(1, controller.getUsers().size());
        assertEquals(1, user.getId());

        User user2 = User.builder()
                .name("Mike")
                .email("mike777@gyandex.ru")
                .login("Mike777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        controller.createUser(user2);
        assertEquals(2, controller.getUsers().size());
        assertEquals(2, user2.getId());
    }

    @Test
    public void createExistedUser() {
        User namelessUser = User.builder()
                .name("")
                .email("snowbaby357@gyandex.ru")
                .login("snezhana257")
                .birthday(LocalDate.of(1989, 7, 7))
                .build();
        controller.createUser(namelessUser);

        User existedIdUser = User.builder()
                .name("Snezhana")
                .email("snowbaby357@gyandex.ru")
                .id(1)
                .login("snezhana257")
                .birthday(LocalDate.of(1989, 7, 7))
                .build();
        UserAlreadyExistException exception6 = assertThrows(UserAlreadyExistException.class,
                () -> controller.createUser(existedIdUser));
        assertEquals("error, the user already exists", exception6.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    public void createNamelessUser() {
        User namelessUser = User.builder()
                .name("")
                .email("snowbaby357@gyandex.ru")
                .login("snezhana257")
                .birthday(LocalDate.of(1989, 7, 7))
                .build();
        controller.createUser(namelessUser);
        assertEquals(1, controller.getUsers().size());
        assertEquals("snezhana257", controller.getUsers().get(0).getName());
    }

    @Test
    public void createFutureBirthdayUser() {
        User futureBirthdayUser = User.builder()
                .name("Mike")
                .email("mike777gyandex@.ru")
                .login("mike777")
                .birthday(LocalDate.of(2100, 7, 7))
                .build();
        UserValidationException exception5 = assertThrows(UserValidationException.class,
                () -> controller.createUser(futureBirthdayUser));
        assertEquals("bad birthday", exception5.getMessage());
        assertEquals(0, controller.getUsers().size());
    }

    @Test
    public void createSpacedLoginUser() {
        User spacedLoginUser = User.builder()
                .name("Mike")
                .email("mike777gyandex@.ru")
                .login("mike 777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception4 = assertThrows(UserValidationException.class,
                () -> controller.createUser(spacedLoginUser));
        assertEquals("bad login", exception4.getMessage());
        assertEquals(0, controller.getUsers().size());
    }

    @Test
    public void createNoEmailUser() {
        User emptyMailUser = User.builder()
                .name("Mike")
                .email("")
                .login("Mike777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception = assertThrows(UserValidationException.class,
                () -> controller.createUser(emptyMailUser));
        assertEquals("bad email", exception.getMessage());
        assertEquals(0, controller.getUsers().size());
    }

    @Test
    public void createWrongEmailUser() {
        User wrongMailUser = User.builder()
                .name("Mike")
                .email("mike777gyandex.ru")
                .login("Mike777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception2 = assertThrows(UserValidationException.class,
                () -> controller.createUser(wrongMailUser));
        assertEquals("bad email", exception2.getMessage());
        assertEquals(0, controller.getUsers().size());
    }

    @Test
    public void createNoLoginUser() {
        User emptyLoginUser = User.builder()
                .name("Mike")
                .email("mike777gyandex@.ru")
                .login("")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception3 = assertThrows(UserValidationException.class,
                () -> controller.createUser(emptyLoginUser));
        assertEquals("bad login", exception3.getMessage());
        assertEquals(0, controller.getUsers().size());
    }

    @Test
    public void updateUser() {
        User user = User.builder()
                .name("John")
                .email("john666@gyandex.ru")
                .login("John666")
                .birthday(LocalDate.of(1966, 6, 6))
                .build();
        controller.createUser(user);

        User user2 = User.builder()
                .name("Mike")
                .email("mike777@gyandex.ru")
                .login("Mike777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        controller.createUser(user2);

        user2.setName("Michael");
        user2.setEmail("mike777@yagoogle.ru");
        user2.setLogin("Michael777");
        user2.setBirthday(LocalDate.of(1992, 5, 8));
        controller.updateUser(user2);
        assertEquals(2, controller.getUsers().size());
        assertEquals(2, user2.getId());
        assertEquals("Michael", controller.getUsers().get(1).getName());
        assertEquals("mike777@yagoogle.ru", controller.getUsers().get(1).getEmail());
        assertEquals("Michael777", controller.getUsers().get(1).getLogin());
        assertEquals(LocalDate.of(1992, 5, 8), controller.getUsers().get(1).getBirthday());

        User notExistedUser = User.builder()
                .name("Noone")
                .email("noonepetrovich@gyandex.ru")
                .login("astralpetrovich228")
                .id(28)
                .birthday(LocalDate.of(1928, 2, 7))
                .build();

        UserNotExistException exception = assertThrows(UserNotExistException.class,
                () -> controller.updateUser(notExistedUser));
        assertEquals("error, the user doesn't exist", exception.getMessage());
        assertEquals(2, controller.getUsers().size());
    }

    @Test
    public void updateNotExistedUser() {
        User notExistedUser = User.builder()
                .name("Noone")
                .email("noonepetrovich@gyandex.ru")
                .login("astralpetrovich228")
                .id(28)
                .birthday(LocalDate.of(1928, 2, 7))
                .build();

        UserNotExistException exception = assertThrows(UserNotExistException.class,
                () -> controller.updateUser(notExistedUser));
        assertEquals("error, the user doesn't exist", exception.getMessage());
        assertEquals(0, controller.getUsers().size());
    }

    @Test
    void addingToFriends() {
        User user = User.builder()
                .email("zachbraff@scrubs.com")
                .login("jd29")
                .name("John")
                .birthday(LocalDate.of(1975, 4, 6))
                .build();
        controller.createUser(user);
        assertEquals(0, user.getFriends().size());

        User user2 = User.builder()
                .email("donaldfaison@scrubs.com")
                .login("chocolatebear69")
                .name("Turk")
                .birthday(LocalDate.of(1974, 6, 22))
                .build();
        controller.createUser(user2);
        controller.addFriend(user.getId(), user2.getId());
        assertTrue(user2.getFriends().contains(1));
        assertTrue(user.getFriends().contains(2));
        assertEquals(1, user.getFriends().size());

        User user3 = User.builder()
                .email("sarahchalke@scrubs.com")
                .login("flatazz")
                .name("Elliot")
                .birthday(LocalDate.of(1976, 8, 27))
                .build();
        controller.createUser(user3);
        controller.addFriend(user.getId(), user3.getId());
        assertTrue(user3.getFriends().contains(1));
        assertTrue(user.getFriends().contains(3));
        assertEquals(2, user.getFriends().size());

        controller.removeFriend(user.getId(), user2.getId());
        assertFalse(user2.getFriends().contains(1));
        assertFalse(user.getFriends().contains(2));
        assertEquals(1, user.getFriends().size());
    }
}
