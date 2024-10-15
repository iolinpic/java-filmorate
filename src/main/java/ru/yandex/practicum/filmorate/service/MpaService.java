package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public Mpa findById(Long id) {
        Mpa mpa = mpaRepository.findById(id);
        if (mpa == null) {
            throw new NotFoundException("Mpa not found");
        }
        return mpa;
    }

    public Collection<Mpa> findAll() {
        return mpaRepository.getAll();
    }
}
