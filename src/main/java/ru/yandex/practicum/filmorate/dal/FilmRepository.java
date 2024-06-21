package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY = "SELECT f.film_id,\n" +
            "   f.name,\n" +
            "   f.description,\n" +
            "   f.releaseDate,\n" +
            "   f.duration,\n" +
            "   mr.mpaa_id,\n" +
            "   mr.mpaa\n" +
            "FROM films as f\n" +
            "JOIN filmmpaa as fm ON f.film_id=fm.fm_film_id\n" +
            "JOIN mpaa_ratings as mr ON fm.fm_mpaa_id=mr.mpaa_id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.film_id,\n" +
            "   f.name,\n" +
            "   f.description,\n" +
            "   f.releaseDate,\n" +
            "   f.duration,\n" +
            "   mr.mpaa_id,\n" +
            "   mr.mpaa\n" +
            "FROM films as f\n" +
            "JOIN filmmpaa as fm ON f.film_id=fm.fm_film_id\n" +
            "JOIN mpaa_ratings as mr ON fm.fm_mpaa_id=mr.mpaa_id\n" +
            "WHERE f.film_id = ?";
    private static final String FIND_POPULAR_FILMS = "SELECT f.film_id,\n" +
            "   f.name,\n" +
            "   f.description,\n" +
            "   f.releaseDate,\n" +
            "   f.duration,\n" +
            "   mr.mpaa_id,\n" +
            "   mr.mpaa,\n" +
            "   COUNT(l.l_film_id)\n" +
            "FROM likes as l\n" +
            "JOIN films as f ON f.film_id=l.l_film_id\n" +
            "JOIN filmmpaa as fm ON f.film_id=fm.fm_film_id\n" +
            "JOIN mpaa_ratings as mr ON fm.fm_mpaa_id=mr.mpaa_id\n" +
            "GROUP BY f.film_id, mr.mpaa_id, mr.mpaa\n" +
            "ORDER BY COUNT(l.l_film_id) DESC\n" +
            "LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, duration)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films" +
            " SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE film_id = ?";
    private static final String DELETE = "DELETE FROM films WHERE film_id = ?";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film findById(long filmId) {
        Optional<Film> searchResult = findOne(FIND_BY_ID_QUERY, filmId);
        if (searchResult.isPresent()) {
            return searchResult.get();
        } else {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return findMany(FIND_POPULAR_FILMS, count);
    }

    public Film save(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        findById(film.getId());
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        return film;
    }

    public boolean delete(long filmId) {
        return delete(DELETE, filmId);
    }
}
