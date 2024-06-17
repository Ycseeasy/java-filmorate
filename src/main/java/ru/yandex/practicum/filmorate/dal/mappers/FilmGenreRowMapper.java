package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {
    @Override
    public FilmGenre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setId(resultSet.getLong("filmGenre_id"));
        filmGenre.setFilmId(resultSet.getLong("fg_film_id"));
        filmGenre.setGenreId(resultSet.getLong("fg_genre_id"));
        return filmGenre;
    }
}
