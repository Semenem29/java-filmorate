package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    private UserController controller;

    @BeforeEach
    public void createController() {
        controller = new UserController();
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

        User emptyMailUser = User.builder()
                .name("Mike")
                .email("")
                .login("Mike777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception = assertThrows(UserValidationException.class,
                () -> controller.createUser(emptyMailUser));
        assertEquals("bad email", exception.getMessage());
        assertEquals(2, controller.getUsers().size());

        User wrongMailUser = User.builder()
                .name("Mike")
                .email("mike777gyandex.ru")
                .login("Mike777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception2 = assertThrows(UserValidationException.class,
                () -> controller.createUser(wrongMailUser));
        assertEquals("bad email", exception2.getMessage());
        assertEquals(2, controller.getUsers().size());

        User emptyLoginUser = User.builder()
                .name("Mike")
                .email("mike777gyandex@.ru")
                .login("")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception3 = assertThrows(UserValidationException.class,
                () -> controller.createUser(emptyLoginUser));
        assertEquals("bad login", exception3.getMessage());
        assertEquals(2, controller.getUsers().size());

        User spacedLoginUser = User.builder()
                .name("Mike")
                .email("mike777gyandex@.ru")
                .login("mike 777")
                .birthday(LocalDate.of(1977, 7, 7))
                .build();
        UserValidationException exception4 = assertThrows(UserValidationException.class,
                () -> controller.createUser(spacedLoginUser));
        assertEquals("bad login", exception4.getMessage());
        assertEquals(2, controller.getUsers().size());

        User futureBirthdayUser = User.builder()
                .name("Mike")
                .email("mike777gyandex@.ru")
                .login("mike777")
                .birthday(LocalDate.of(2100, 7, 7))
                .build();
        UserValidationException exception5 = assertThrows(UserValidationException.class,
                () -> controller.createUser(futureBirthdayUser));
        assertEquals("bad birthday", exception5.getMessage());
        assertEquals(2, controller.getUsers().size());

        User namelessUser = User.builder()
                .name("")
                .email("snowbaby357@gyandex.ru")
                .login("snezhana257")
                .birthday(LocalDate.of(1989, 7, 7))
                .build();
        controller.createUser(namelessUser);
        assertEquals(3, controller.getUsers().size());
        assertEquals("snezhana257", controller.getUsers().get(2).getName());

        User existedIdUser = User.builder()
                .name("Snezhana")
                .email("snowbaby357@gyandex.ru")
                .id(2)
                .login("snezhana257")
                .birthday(LocalDate.of(1989, 7, 7))
                .build();
        UserAlreadyExistException exception6 = assertThrows(UserAlreadyExistException.class,
                () -> controller.createUser(existedIdUser));
        assertEquals("error, the user already exists", exception6.getMessage());
        assertEquals(3, controller.getUsers().size());
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
}
