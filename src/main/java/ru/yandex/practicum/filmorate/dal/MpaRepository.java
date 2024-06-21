package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM Mpaa_ratings";
    private static final String FIND_BY_ID = "SELECT * FROM Mpaa_ratings WHERE Mpaa_id = ?";
    private static final String FIND_BY_FILM = "Select mr.mpaa_id,\n" +
            "   mr.mpaa\n" +
            "FROM filmMpaa as fm\n" +
            "JOIN mpaa_ratings as mr On fm.fm_mpaa_id=mr.mpaa_id\n" +
            "WHERE fm.fm_film_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAllMpa() {
        return findMany(FIND_ALL_QUERY);
    }

    public Mpa findById(long id) {
        Optional<Mpa> searchResult = findOne(FIND_BY_ID, id);
        return searchResult.orElse(null);
    }

    public Optional<Mpa> findByFilm(long filmId) {
        return findOne(FIND_BY_FILM, filmId);
    }
}
