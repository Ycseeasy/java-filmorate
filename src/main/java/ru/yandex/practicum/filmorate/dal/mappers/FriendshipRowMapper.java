package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friendship friendShip = new Friendship();
        friendShip.setId(resultSet.getLong("friendship_id"));
        friendShip.setUserId(resultSet.getLong("fs_user_id"));
        friendShip.setFriendId(resultSet.getLong("fs_friend_id"));
        return friendShip;
    }
}
