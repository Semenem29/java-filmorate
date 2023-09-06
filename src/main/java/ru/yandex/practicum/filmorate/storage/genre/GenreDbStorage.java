package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.util.FilmorateMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, FilmorateMapper::genreFromRow);
    }

    @Override
    public Genre getGenreById(int id) {
        try {
            String sqlQuery = "select * from genre where genre_id = ?";
            return jdbcTemplate.queryForObject(
                    sqlQuery,
                    FilmorateMapper::genreFromRow,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotExistException(String.format("Genre was not found with id=%d", id));
        }
    }

    @Override
    public Film addFilmGenres(Film film) {
        List<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            genres = genres.stream()
                    .distinct()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            film.setGenres(genres);
            String sqlQuery = "insert into film_genre (film_id, genre_id) values (?, ?)";
            int filmId = film.getId();
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery, filmId, genre.getId()));
        }
        return film;
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        String sqlQuery = "select * from genre g join film_genre fg on g.genre_id = fg.genre_id where fg.film_id = ?";
        return jdbcTemplate.query(
                sqlQuery,
                FilmorateMapper::genreFromRow,
                filmId
        );
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
