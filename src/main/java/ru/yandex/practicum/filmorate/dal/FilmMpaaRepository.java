package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmMpaa;

import java.util.List;


@Repository
public class FilmMpaaRepository extends BaseRepository<FilmMpaa> {

    private static final String INSERT_QUERY = "INSERT INTO filmMpaa(fm_film_id, fm_mpaa_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM filmMpaa WHERE fm_film_id = ? AND fm_mpaa_id = ?";
    private static final String FIND_ALL = "SELECT * FROM filmMpaa";

    public FilmMpaaRepository(JdbcTemplate jdbc, RowMapper<FilmMpaa> mapper) {
        super(jdbc, mapper);
    }

    public FilmMpaa save(long filmId, long mpaaId) {
        insertWithoutId(INSERT_QUERY,
                filmId,
                mpaaId
        );
        return new FilmMpaa(filmId, mpaaId);
    }

    public boolean deleteMpaaFromFilm(long filmId, long mpaaId) {
        return delete(DELETE, filmId, mpaaId);
    }

    public List<FilmMpaa> findAll() {
        return findMany(FIND_ALL);
    }
}
