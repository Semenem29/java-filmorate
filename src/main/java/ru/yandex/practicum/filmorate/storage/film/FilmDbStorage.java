package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmorateMapper;

import java.util.List;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getStorage() {
        String sqlQuery =
                "select f.*, RATING.name as mpa_name from film as f left join RATING on f.RATING_ID = RATING.RATING_ID";
        return jdbcTemplate.query(sqlQuery, FilmorateMapper::filmFromRow);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(FilmorateMapper.filmToRow(film)).intValue());
        log.info("{} was added", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update film set name = ?, description = ?, release_date = ?, " +
                "duration = ?, rating_id = ? where film_id = ?";
        int filmId = film.getId();
        int rowsUpdated = jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                filmId
        );
        if (rowsUpdated == 1) {
            log.info("{} was updated", film);
            return film;
        } else {
            String message = String.format("Film with id=%d wasn't found", filmId);
            log.error(message);
            throw new FilmNotExistException(message);
        }
    }

    @Override
    public Film getFilmById(int filmId) {
        try {
            String sqlQuery = "select f.*, rating.name as mpa_name from film as f " +
                    "left join rating on f.rating_id = rating.RATING_ID where f.FILM_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, FilmorateMapper::filmFromRow, filmId);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Film with id=%d wasn't found", filmId);
            log.error(message);
            throw new FilmNotExistException(message);
        }
    }

    @Override
    public List<Film> getTop(int count) {
        String sqlQuery =
                "select f.*, rating.name as mpa_name from film as f " +
                        "left join rating on f.RATING_ID = RATING.RATING_ID " +
                        "left join FILM_USER on f.FILM_ID = FILM_USER.FILM_ID " +
                        "group by FILM_USER.FILM_ID, f.FILM_ID " +
                        "order by count(FILM_USER.USER_ID) desc, FILM_USER.FILM_ID " +
                        "limit ? ";

        return jdbcTemplate.query(sqlQuery, FilmorateMapper::filmFromRow, count);
    }
}
