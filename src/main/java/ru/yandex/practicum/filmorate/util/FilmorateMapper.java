package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FilmorateMapper {

    public static Map<String, Object> filmToRow(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        return values;
    }

    public static Film filmFromRow(ResultSet rsFilm, int rowNumFilm) throws SQLException {
        Film film = new Film(
                rsFilm.getInt("film_id"),
                rsFilm.getString("name"),
                rsFilm.getString("description"),
                LocalDate.parse(rsFilm.getString("release_date")),
                rsFilm.getInt("duration"),
                new HashSet<>(),
                0,
                new RatingMPA(rsFilm.getInt("rating_id"), rsFilm.getString("mpa_name")),
                new ArrayList<>()
        );
        film.setId(rsFilm.getInt("film_id"));
        return film;
    }

    public static Map<String, Object> userToRow(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }

    public static User userFromRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                LocalDate.parse(rs.getString("birthday")),
                new HashSet<>()
        );
    }

    public static Genre genreFromRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("genre_id"), rs.getString("name")
        );
    }

    public static RatingMPA ratingMPAFromRow(ResultSet rs, int rowNum) throws SQLException {
        return new RatingMPA(
                rs.getInt("rating_id"), rs.getString("name")
        );
    }
}
