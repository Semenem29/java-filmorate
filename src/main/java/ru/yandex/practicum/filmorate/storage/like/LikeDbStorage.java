package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery =
                "merge into FILM_USER (film_id, user_id) KEY (FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Film with id={} was liked by user with id={}", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery =
                "delete from FILM_USER where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Film with id={} was unliked by user with id={}", filmId, userId);
    }

    @Override
    public List<Integer> getFans(int filmId) {
        String sqlQuery =
                "select user_id from film_user where film_id = ?";
        return jdbcTemplate.query(
                sqlQuery,
                (rs, rowNum) -> rs.getInt("user_id"),
                filmId
        );
    }
}
