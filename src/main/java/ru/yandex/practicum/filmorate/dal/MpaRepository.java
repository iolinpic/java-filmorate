package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }


    public Collection<Mpa> getAll() {
        return findMany(FIND_ALL_QUERY);
    }


    public Mpa findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

}
