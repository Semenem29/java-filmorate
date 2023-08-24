package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    private FilmController controller;

    @BeforeEach
    public void createController() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage(), new UserService(new InMemoryUserStorage())));
    }

    @Test
    public void addFilm() {
        assertEquals(0, controller.getFilmStorage().size());

        Film film = Film.builder()
                .name("Die Hard")
                .description("40 Storeys High - with Suspense, Excitement and Adventure on every level!")
                .releaseDate(LocalDate.of(1988, 1, 1))
                .duration(133)
                .build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilmStorage().size());
        assertEquals(1, film.getId());

        Film film2 = Film.builder()
                .name("Die Hard 2")
                .description("Yippee Ki Yay, All over again!")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(124)
                .build();
        controller.addFilm(film2);
        assertEquals(2, controller.getFilmStorage().size());
        assertEquals(2, film2.getId());

        Film blankNameFilm = Film.builder()
                .name("")
                .description("some description")
                .releaseDate(LocalDate.of(1990, 11, 1))
                .duration(120)
                .build();
        FilmValidationException exception = assertThrows(FilmValidationException.class,
                () -> controller.addFilm(blankNameFilm));
        assertEquals("bad name was provided", exception.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        Film longDescriptionFilm = Film.builder()
                .name("Гарри Поттер")
                .description("Жизнь десятилетнего Гарри Поттера нельзя назвать сладкой: родители умерли, " +
                        "едва ему исполнился год, а от дяди и тёти, взявших сироту на воспитание, " +
                        "достаются лишь тычки да подзатыльники. Но в одиннадцатый день рождения " +
                        "Гарри всё меняется. Странный гость, неожиданно появившийся на пороге, " +
                        "приносит письмо, из которого мальчик узнаёт, что на самом деле он - " +
                        "волшебник и зачислен в школу магии под названием Хогвартс. " +
                        "А уже через пару недель Гарри будет мчаться в поезде Хогвартс-экспресс навстречу новой жизни, " +
                        "где его ждут невероятные приключения, верные друзья и самое главное — " +
                        "ключ к разгадке тайны смерти его родителей.")
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(152)
                .build();
        FilmValidationException exception2 = assertThrows(FilmValidationException.class,
                () -> controller.addFilm(longDescriptionFilm));
        assertEquals("too long description. it should be less or equals 200 letters", exception2.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        Film earlyDateFilm = Film.builder()
                .name("Пушкин")
                .description("Запись рождения Сергеича")
                .releaseDate(LocalDate.of(1799, 6, 6))
                .duration(60)
                .build();
        FilmValidationException exception3 = assertThrows(FilmValidationException.class,
                () -> controller.addFilm(earlyDateFilm));
        assertEquals("bad release date", exception3.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        Film negativeFilm = Film.builder()
                .name("Абсолютный ноль")
                .description("Кто круче Кельвин или Цельсий?")
                .releaseDate(LocalDate.of(2033, 4, 6))
                .duration(-273)
                .build();
        FilmValidationException exception4 = assertThrows(FilmValidationException.class,
                () -> controller.addFilm(negativeFilm));
        assertEquals("bad duration", exception4.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        Film existedIdFilm = Film.builder()
                .name("Die Die Haaaard")
                .description("Another descrip")
                .id(2)
                .releaseDate(LocalDate.of(2025, 4, 6))
                .duration(144)
                .build();
        FilmAlreadyExistException exception5 = assertThrows(FilmAlreadyExistException.class,
                () -> controller.addFilm(existedIdFilm));
        assertEquals("error, the film already exists", exception5.getMessage());
        assertEquals(2, controller.getFilmStorage().size());
    }

    @Test
    public void updateFilm() {
        assertEquals(0, controller.getFilmStorage().size());

        Film film = Film.builder()
                .name("Die Hard")
                .description("40 Storeys High - with Suspense, Excitement and Adventure on every level!")
                .releaseDate(LocalDate.of(1988, 1, 1))
                .duration(133)
                .build();
        controller.addFilm(film);

        Film film2 = Film.builder()
                .name("Die Hard 2")
                .description("Yippee Ki Yay, All over again!")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(124)
                .build();
        controller.addFilm(film2);

        film2.setName("Крепкий орешек 2");
        film2.setDescription("Бла Бла БЛа, Брюс Уиллис всех победит");
        film2.setReleaseDate(LocalDate.of(1991, 2, 3));
        film2.setDuration(120);
        controller.updateFilm(film2);
        assertEquals(2, film2.getId());
        assertEquals(2, controller.getFilmStorage().size());
        assertEquals(120, controller.getFilmStorage().get(1).getDuration());
        assertEquals(LocalDate.of(1991, 2, 3), controller.getFilmStorage().get(1).getReleaseDate());
        assertEquals("Крепкий орешек 2", controller.getFilmStorage().get(1).getName());
        assertEquals("Бла Бла БЛа, Брюс Уиллис всех победит", controller.getFilmStorage().get(1).getDescription());

        film2.setName("");
        FilmValidationException exception = assertThrows(FilmValidationException.class,
                () -> controller.updateFilm(film2));
        assertEquals("bad name was provided", exception.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        film2.setName("Крепкий орешек 2");
        film2.setDescription("Жизнь десятилетнего Гарри Поттера нельзя назвать сладкой: родители умерли, \" +\n" +
                "                        \"едва ему исполнился год, а от дяди и тёти, взявших сироту на воспитание, \" +\n" +
                "                        \"достаются лишь тычки да подзатыльники. Но в одиннадцатый день рождения \" +\n" +
                "                        \"Гарри всё меняется. Странный гость, неожиданно появившийся на пороге, \" +\n" +
                "                        \"приносит письмо, из которого мальчик узнаёт, что на самом деле он - \" +\n" +
                "                        \"волшебник и зачислен в школу магии под названием Хогвартс. \" +\n" +
                "                        \"А уже через пару недель Гарри будет мчаться в поезде Хогвартс-экспресс навстречу новой жизни, \" +\n" +
                "                        \"где его ждут невероятные приключения, верные друзья и самое главное — \" +\n" +
                "                        \"ключ к разгадке тайны смерти его родителей.");

        FilmValidationException exception2 = assertThrows(FilmValidationException.class,
                () -> controller.updateFilm(film2));
        assertEquals("too long description. it should be less or equals 200 letters", exception2.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        film2.setDescription("Бла Бла БЛа, Брюс Уиллис всех победит");
        film2.setReleaseDate(LocalDate.of(1799, 6, 6));
        FilmValidationException exception3 = assertThrows(FilmValidationException.class,
                () -> controller.updateFilm(film2));
        assertEquals("bad release date", exception3.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        film2.setReleaseDate(LocalDate.of(1991, 2, 3));
        film2.setDuration(-273);
        FilmValidationException exception4 = assertThrows(FilmValidationException.class,
                () -> controller.updateFilm(film2));
        assertEquals("bad duration", exception4.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

        film2.setDuration(120);

        Film notExistedFilm = Film.builder()
                .name("Die Hard 777")
                .description("Be Health Dear Bruce")
                .id(777)
                .releaseDate(LocalDate.of(2077, 1, 1))
                .duration(180)
                .build();
        FilmNotExistException exception5 = assertThrows(FilmNotExistException.class,
                () -> controller.updateFilm(notExistedFilm));
        assertEquals("error, the film doesn't exist", exception5.getMessage());
        assertEquals(2, controller.getFilmStorage().size());

    }
}
