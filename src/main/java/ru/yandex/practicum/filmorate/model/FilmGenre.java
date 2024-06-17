package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmGenre {

    private long id;
    private long filmId;
    private long genreId;

    public FilmGenre(long filmId, long genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }
}
