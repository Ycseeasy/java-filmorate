package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private long id;
    private long whoLikedId;
    private long whatLikedId;

    public Like(long userId, long filmId) {
        this.whoLikedId = userId;
        this.whatLikedId = filmId;
    }
}
