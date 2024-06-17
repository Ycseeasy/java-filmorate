package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmMpaa;


@Repository
public class FilmMpaaRepository extends BaseRepository<FilmMpaa> {

    private static final String INSERT_QUERY = "INSERT INTO filmMpaa(fm_film_id, fm_mpaa_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM filmMpaa WHERE filmMpaa_id = ?";

    public FilmMpaaRepository(JdbcTemplate jdbc, RowMapper<FilmMpaa> mapper) {
        super(jdbc, mapper);
    }

    public FilmMpaa save(long filmId, long mpaaId) {
        long id = insert(INSERT_QUERY,
                filmId,
                mpaaId
        );
        FilmMpaa filmMpaa = new FilmMpaa(filmId, mpaaId);
        filmMpaa.setId(id);
        return filmMpaa;
    }

    public boolean deleteMpaaFromFilm(long id) {
        return delete(DELETE, id);
    }
}
