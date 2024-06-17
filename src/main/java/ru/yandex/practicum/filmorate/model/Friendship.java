package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    private long id;
    private long userId;
    private long friendId;

    public Friendship(long userId, long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
