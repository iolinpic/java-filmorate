package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Objects;

@Repository("FilmRepository")
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String ADD_QUERY = "INSERT INTO films (name, description, releaseDate, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name=?,description=?,releaseDate=?,duration=?,rating_id=? WHERE id=?";

    private final FilmGenreRepository filmGenreRepository;
    private final LikeRepository filmLikeRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper,
                          @Autowired FilmGenreRepository filmGenreRepository,
                          @Autowired LikeRepository filmLikeRepository,
                          @Autowired MpaRepository mpaRepository,
                          @Autowired GenreRepository genreRepository) {
        super(jdbc, mapper);
        this.filmGenreRepository = filmGenreRepository;
        this.filmLikeRepository = filmLikeRepository;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Film> getFilms() {
        Collection<Film> films = findMany(FIND_ALL_QUERY);
        if (!films.isEmpty()) {
            Collection<FilmGenre> filmGenreCollection = filmGenreRepository.getAll();
            Collection<Like> likeCollection = filmLikeRepository.getAll();
            Collection<Mpa> mpaCollection = mpaRepository.getAll();
            Collection<Genre> genreCollection = genreRepository.getAll();
            for (Film film : films) {
                film.getGenres().addAll(filmGenreCollection
                        .stream()
                        .filter(filmGenre -> Objects.equals(film.getId(), filmGenre.getFilmId()))
                        .map(filmGenre -> Genre.builder()
                                .id(filmGenre.getGenreId())
                                .name(genreCollection
                                        .stream()
                                        .filter(genre -> genre.getId().equals(filmGenre.getGenreId())).findFirst().get().getName())
                                .build())
                        .toList());
                film.getLikes().addAll(likeCollection.stream().filter(like -> Objects.equals(like.getFilmId(), film.getId())).map(Like::getUserId).toList());
                film.getMpa().setName(mpaCollection.stream().filter(mpa -> Objects.equals(film.getMpa().getId(), mpa.getId())).findFirst().get().getName());
            }
        }
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        Film film = findOne(FIND_BY_ID_QUERY, id);
        if (film != null) {
            Collection<Genre> genreCollection = genreRepository.getAll();
            film.getGenres().addAll(filmGenreRepository
                    .findAllByFilmId(film.getId())
                    .stream()
                    .map(filmGenre -> Genre.builder()
                            .id(filmGenre.getGenreId())
                            .name(genreCollection.stream().filter(genre -> genre.getId().equals(filmGenre.getGenreId())).findFirst().get().getName())
                            .build())
                    .toList());
            film.getLikes().addAll(filmLikeRepository.findAllByFilmId(film.getId()).stream().map(Like::getUserId).toList());
            film.getMpa().setName(mpaRepository.findById(film.getMpa().getId()).getName());
        }
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        Long id = insert(ADD_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        if (!film.getGenres().isEmpty()) {
            filmGenreRepository.addGenresToFilm(id, film.getGenres().stream().map(Genre::getId).toList());
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (!film.getGenres().isEmpty()) {
            filmGenreRepository.deleteGenresFromFilm(film.getId());
            filmGenreRepository.addGenresToFilm(film.getId(), film.getGenres().stream().map(Genre::getId).toList());
        }
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmLikeRepository.addLikeToFilm(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        filmLikeRepository.deleteLikeFromFilm(filmId, userId);
    }

    @Override
    public void deleteFilm(Long id) {
        delete(DELETE_QUERY, id);
    }
}
