package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.FilmorateMapper;

import java.util.List;

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getStorage() {
        return jdbcTemplate.query("select * from users", FilmorateMapper::userFromRow);
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(FilmorateMapper.userToRow(user)).intValue());
        log.info("{} was added", user);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        int userId = user.getId();
        int rowUpdated = jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                userId
        );
        if (rowUpdated == 1) {
            log.info("{} was updated", user);
            return user;
        } else {
            throw new UserNotExistException(String.format("User was not found with id=%d", userId));
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            return jdbcTemplate.queryForObject("select * from users where user_id = ?",
                    FilmorateMapper::userFromRow, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistException(String.format("User was not found with id=%d", id));
        }
    }
}
