package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.yandex.practicum.filmorate.utils.DateUtils.toLocaldate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(toLocaldate(resultSet.getString("releaseDate")));
        film.setDuration(resultSet.getLong("duration"));
        Mpa mpa = new Mpa(resultSet.getLong("mpaa_id"), resultSet.getString("mpaa"));
        film.setMpa(mpa);
        return film;
    }
}
