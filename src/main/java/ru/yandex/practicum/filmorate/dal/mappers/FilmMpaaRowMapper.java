package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmMpaa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmMpaaRowMapper implements RowMapper<FilmMpaa> {
    @Override
    public FilmMpaa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmMpaa filmMpa = new FilmMpaa();
        filmMpa.setFilmId(resultSet.getLong("fm_film_id"));
        filmMpa.setMpaaId(resultSet.getLong("fm_mpaa_id"));
        return filmMpa;
    }
}
