package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {

    private static final String INSERT_QUERY = "INSERT INTO filmGenre(fg_film_id, fg_genre_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM filmGenre WHERE fg_film_id = ? AND fg_genre_id = ?";
    private static final String FIND_ALL = "SELECT * FROM filmGenre";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public FilmGenre save(long filmId, long genreId) {
        insertWithoutId(INSERT_QUERY,
                filmId,
                genreId);
        return new FilmGenre(filmId, genreId);
    }

    public boolean deleteGenreFromFilm(long filmId, long genreId) {
        return delete(DELETE, filmId, genreId);
    }

    public List<FilmGenre> findAll() {
        return findMany(FIND_ALL);
    }
}
