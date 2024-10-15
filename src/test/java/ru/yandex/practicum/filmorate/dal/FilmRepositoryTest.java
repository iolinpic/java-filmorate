package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FilmGenreMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.FriendshipMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Import({FilmRepository.class, FilmMapper.class,
        FilmGenreRepository.class, FilmGenreMapper.class,
        LikeRepository.class, LikeMapper.class,
        MpaRepository.class, MpaMapper.class,
        GenreRepository.class, GenreMapper.class,
        UserRepository.class, UserMapper.class,
        FriendshipRepository.class, FriendshipMapper.class,
})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmRepositoryTest {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    private Film createFilm() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setDuration(0);
        film.setReleaseDate(LocalDate.now());
        film.setMpa(new Mpa(1L, ""));
        return film;
    }

    @Test
    void shouldReturnEmptyFilmList() {
        Collection<Film> films = filmRepository.getFilms();
        assertTrue(films.isEmpty());
    }

    @Test
    void shouldReturnFilmList() {
        Film film = createFilm();
        filmRepository.addFilm(film);
        Collection<Film> films = filmRepository.getFilms();
        assertEquals(1, films.size());
    }

    @Test
    void shouldReturnFilmById() {
        Film film = createFilm();
        film = filmRepository.addFilm(film);
        Film film2 = filmRepository.getFilm(film.getId());
        assertEquals(film.getName(), film2.getName());
    }

    @Test
    void shouldSuccessfullyAddFilm() {
        Film film = createFilm();
        film = filmRepository.addFilm(film);
        assertNotEquals(0L, film.getId());
    }

    @Test
    void shouldSuccessfullyUpdateFilm() {
        Film film = createFilm();
        film = filmRepository.addFilm(film);
        film.setName("TestUpdate");
        filmRepository.updateFilm(film);
        Film film2 = filmRepository.getFilm(film.getId());
        assertEquals(film.getName(), film2.getName());
    }

    @Test
    void addLike() {
        User user = new User();
        user.setBirthday(LocalDate.now());
        user.setLogin("login");
        user.setEmail("email@test.ry");
        user.setName("test");
        user = userRepository.addUser(user);
        Film film = createFilm();
        film = filmRepository.addFilm(film);

        filmRepository.addLike(film.getId(), user.getId());
        film = filmRepository.getFilm(film.getId());
        assertEquals(1, film.getLikes().size());
    }

    @Test
    void removeLike() {
        User user = new User();
        user.setBirthday(LocalDate.now());
        user.setLogin("login");
        user.setEmail("email@test.ry");
        user.setName("test");
        user = userRepository.addUser(user);
        Film film = createFilm();
        film = filmRepository.addFilm(film);

        filmRepository.addLike(film.getId(), user.getId());
        filmRepository.removeLike(film.getId(), user.getId());
        film = filmRepository.getFilm(film.getId());
        assertEquals(0, film.getLikes().size());
    }

    @Test
    void shouldSuccessfullyRemoveFilm() {
        Film film = createFilm();
        film = filmRepository.addFilm(film);
        filmRepository.deleteFilm(film.getId());
        Film film2 = filmRepository.getFilm(film.getId());
        assertNull(film2);
    }
}