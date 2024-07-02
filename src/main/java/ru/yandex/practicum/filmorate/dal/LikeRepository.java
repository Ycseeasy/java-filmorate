package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
import java.util.Optional;

@Repository
public class LikeRepository extends BaseRepository<Like> {

    private static final String FIND_LIKE = "SELECT * FROM likes WHERE l_user_id = ? AND l_film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO likes(l_user_id, l_film_id) " +
            "VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM likes\n" +
            "WHERE l_user_id = ? AND l_film_id = ?";
    private static final String FIND_ALL = "SELECT * FROM likes";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public Like save(Like like) {
        insertWithoutId(INSERT_QUERY,
                like.getWhoLikedId(),
                like.getWhatLikedId());
        return like;
    }

    public boolean deleteLike(long getWhoLikedId, long getWhatLikedId) {
        return delete(DELETE, getWhoLikedId, getWhatLikedId);
    }

    public Optional<Like> findLike(long userId, long filmId) {
        return findOne(FIND_LIKE, userId, filmId);
    }

    public List<Like> findAll() {
        return findMany(FIND_ALL);
    }
}
