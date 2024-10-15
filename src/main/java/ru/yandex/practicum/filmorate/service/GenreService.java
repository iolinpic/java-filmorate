package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre findById(Long id) {
        Genre mpa = genreRepository.findById(id);
        if (mpa == null) {
            throw new NotFoundException("Genre not found");
        }
        return mpa;
    }

    public Collection<Genre> findAll() {
        return genreRepository.getAll();
    }
}
