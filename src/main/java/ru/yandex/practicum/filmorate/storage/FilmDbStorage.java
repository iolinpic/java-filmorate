package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;


public class FilmDbStorage implements FilmStorage {

    @Override
    public Collection<Film> getFilms() {
        return List.of();
    }

    @Override
    public Film getFilm(Long id) {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(Long id) {

    }
}
