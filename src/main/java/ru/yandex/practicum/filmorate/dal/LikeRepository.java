package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Optional;

@Repository
public class LikeRepository extends BaseRepository<Like> {

    private static final String FIND_LIKE = "SELECT * FROM likes WHERE l_user_id = ? AND l_film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO likes(l_user_id, l_film_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM likes\n" +
            "WHERE likes_id = (SELECT likes_id \n" +
            "            FROM likes \n" +
            "            WHERE l_user_id = ? \n" +
            "            AND l_film_id = ?)";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public Like save(Like like) {
        long id = insert(INSERT_QUERY,
                like.getWhoLikedId(),
                like.getWhatLikedId());
        like.setId(id);
        return like;
    }

    public boolean deleteLike(long getWhoLikedId, long getWhatLikedId) {
        return delete(DELETE, getWhoLikedId, getWhatLikedId);
    }

    public Optional<Like> findLike(long userId, long filmId) {
        return findOne(FIND_LIKE, userId, filmId);
    }
}
