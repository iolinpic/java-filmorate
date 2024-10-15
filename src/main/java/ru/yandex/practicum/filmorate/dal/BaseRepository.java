package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.ConstrainsViolationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected T findOne(String query, Object... params) {
        try {
            return jdbc.queryForObject(query, mapper, params);
        } catch (Exception e) {
            return null;
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected boolean delete(String query, Object... params) {
        int rowsDeleted = jdbc.update(query, params);
        return rowsDeleted > 0;
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                for (int idx = 0; idx < params.length; idx++) {
                    ps.setObject(idx + 1, params[idx]);
                }
                return ps;
            }, keyHolder);
        } catch (DataIntegrityViolationException exception) {
            throw new ConstrainsViolationException(exception.getMessage());
        }

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        try {
            int rowsUpdated = jdbc.update(query, params);
            if (rowsUpdated == 0) {
                throw new NotFoundException("Не удалось обновить данные");
            }
        } catch (DataIntegrityViolationException exception) {
            throw new ConstrainsViolationException(exception.getMessage());
        }

    }

    protected void batchUpdate(String query, BatchPreparedStatementSetter bps) {
        try {
            int[] rowsUpdated = jdbc.batchUpdate(query, bps);
            if (rowsUpdated.length == 0) {
                throw new RuntimeException("Не удалось обновить данные");
            }
        } catch (DataIntegrityViolationException exception) {
            throw new ConstrainsViolationException(exception.getMessage());
        }


    }
}
