package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre";
    private static final String FIND_ALL_BY_FILM_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";


    private static final String DELETE_ALL_FROM_FILM_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String ADD_QUERY = "INSERT INTO film_genre (film_id,genre_id) VALUES (?, ?)";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);

    }

    public Collection<FilmGenre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<FilmGenre> findAllByFilmId(long filmId) {
        return findMany(FIND_ALL_BY_FILM_ID_QUERY, filmId);
    }

    public void addGenreToFilm(Long filmId, Long genreId) {
        update(ADD_QUERY, filmId, genreId);
    }

    public void addGenresToFilm(Long filmId, List<Long> genreIds) {
        batchUpdate(ADD_QUERY, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genreIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return genreIds.size();
            }
        });
    }

    public void deleteGenresFromFilm(Long filmId) {
        update(DELETE_ALL_FROM_FILM_QUERY, filmId);
    }


}
