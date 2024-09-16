package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        checkExist(film);
        checkExist(user);
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
        log.info("Added like to film {} by user {}", filmId, userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        checkExist(film);
        checkExist(user);
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
        log.info("Removed like from film {} by user {}", filmId, userId);
        return film;
    }

    private void checkExist(Film film) {
        if (film == null) {
            throw new NotFountException("Film not found");
        }
    }

    private void checkExist(User user) {
        if (user == null) {
            throw new NotFountException("User not found");
        }
    }

    public Collection<Film> getTopFilms(int count) {
        return filmStorage.getFilms().stream().sorted(Comparator.comparing(f -> f.getLikes().size(), Comparator.reverseOrder())).limit(count).toList();
    }
}
