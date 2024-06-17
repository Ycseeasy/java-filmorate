package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {

    private static final String INSERT_QUERY = "INSERT INTO filmGenre(fg_film_id, fg_genre_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM filmGenre WHERE filmGenre_id = ?";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public FilmGenre save(long filmId, long genreId) {
        long id = insert(INSERT_QUERY,
                filmId,
                genreId);
        FilmGenre filmGenre = new FilmGenre(filmId, genreId);
        filmGenre.setId(id);
        return filmGenre;
    }

    public boolean deleteGenreFromFilm(long id) {
        return delete(DELETE, id);
    }
}
