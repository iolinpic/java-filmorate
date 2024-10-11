package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre";
    private static final String FIND_ALL_BY_FILM_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";


    private static final String DELETE_QUERY = "DELETE FROM film_genre WHERE film_id = ? && genre_id = ?";
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

    public void deleteGenreFromFilm(Long filmId, Long genreId) {
        update(DELETE_QUERY, filmId, genreId);
    }


}
