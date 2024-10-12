package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

@Slf4j
@Repository
public class LikeRepository extends BaseRepository<Like> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_like";
    private static final String FIND_ALL_BY_FILM_ID_QUERY = "SELECT * FROM film_like WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
    private static final String ADD_QUERY = "INSERT INTO film_like (film_id,user_id) VALUES (?, ?)";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);

    }

    public Collection<Like> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<Like> findAllByFilmId(long filmId) {
        return findMany(FIND_ALL_BY_FILM_ID_QUERY, filmId);
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        try {
            update(ADD_QUERY, filmId, userId);
            log.info("Added like to film = {} by user = {}", filmId, userId);
        } catch (DuplicateKeyException ignored) {
            log.warn("Like already exist from user_id = {} to film_id = {}", userId, filmId);
        }

    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {
        update(DELETE_QUERY, filmId, userId);
        log.info("Removed like from film = {} by user = {}", filmId, userId);
    }



}
