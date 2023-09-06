package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.RatingMPANotExistException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.util.FilmorateMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingMPADbStorage implements RatingMPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RatingMPA> getAll() {
        String sqlQuery = "select * from rating";
        return jdbcTemplate.query(sqlQuery,
                FilmorateMapper::ratingMPAFromRow);
    }

    @Override
    public RatingMPA getRatingMPAById(int id) {
        try {
            String sqlQuery = "select * from rating where RATING_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, FilmorateMapper::ratingMPAFromRow, id);
        } catch (EmptyResultDataAccessException e) {
            throw new RatingMPANotExistException(String.format("MPA with id=%d wasn't found", id));
        }
    }

}
