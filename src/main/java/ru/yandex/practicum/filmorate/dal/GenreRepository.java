package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT gen.genre_id, gen.genre_name " +
            "FROM filmGenre as fg JOIN genres as gen on fg.fg_genre_id=gen.genre_id WHERE fg.fg_film_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM genres WHERE genre_name = ?";
    private static final String FIND_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public Genre findById(long id) {
        Optional<Genre> searchResult = findOne(FIND_BY_ID, id);
        return searchResult.orElse(null);
    }

    public List<Genre> findFilmsGenres(long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    public Genre findByName(String genreName) {
        Optional<Genre> searchResult = findOne(FIND_BY_NAME_QUERY, genreName);
        return searchResult.orElse(null);
    }
}
