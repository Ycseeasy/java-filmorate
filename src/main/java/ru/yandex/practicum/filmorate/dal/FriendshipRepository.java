package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;

@Repository
public class FriendshipRepository extends BaseRepository<Friendship> {

    private static final String INSERT_QUERY = "INSERT INTO friendship(fs_user_id, fs_friend_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM friendship WHERE friendship_id = ?";
    private static final String FIND_BY_USER_AND_FRIEND_ID_QUERY = "SELECT * FROM friendship " +
            "WHERE fs_user_id = ? AND fs_friend_id = ?";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public Friendship save(Friendship friendship) {
        long id = insert(INSERT_QUERY,
                friendship.getUserId(),
                friendship.getFriendId());
        friendship.setId(id);
        return friendship;
    }

    public boolean deleteFriendship(long friendshipId) {
        return delete(DELETE, friendshipId);
    }

    public Optional<Friendship> findFriendship(long userId, long friendId) {
        return findOne(FIND_BY_USER_AND_FRIEND_ID_QUERY, userId, friendId);
    }
}
