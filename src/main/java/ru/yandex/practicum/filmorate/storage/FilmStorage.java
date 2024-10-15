package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film getFilm(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    void deleteFilm(Long id);
}
