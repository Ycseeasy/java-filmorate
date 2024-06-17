package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmMpaa {

    private long id;
    private long filmId;
    private long mpaaId;

    public FilmMpaa(long filmId, long mpaaId) {
        this.filmId = filmId;
        this.mpaaId = mpaaId;
    }
}
