package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFountException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Add film: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilm(film.getId()) == null) {
            throw new NotFountException("Film not found");
        }
        films.put(film.getId(), film);
        log.info("Update film: {}", film);
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        films.remove(id);
        log.info("Removed film with id: {}", id);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
