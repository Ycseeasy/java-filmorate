package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_USERS = "SELECT * FROM users";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE user_id = ?";
    private static final String GET_FRIEND_BY_ID = "SELECT u.user_id,\n" +
            "   u.email,\n" +
            "   u.login,\n" +
            "   u.name,\n" +
            "   u.birthday\n" +
            "FROM friendship as f\n" +
            "JOIN users as u ON u.user_id=f.fs_friend_id\n" +
            "WHERE f.fs_user_id = ?";
    private static final String DELETE = "DELETE FROM users WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_USERS);
    }

    public User findById(long userId) {
        Optional<User> searchResult = findOne(FIND_BY_ID, userId);
        if (searchResult.isPresent()) {
            return searchResult.get();
        } else {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    public User save(User user) {
        long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        findById(user.getId());
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public boolean delete(long userId) {
        return delete(DELETE, userId);
    }

    public List<User> getFriendList(long userId) {
        return findMany(GET_FRIEND_BY_ID, userId);
    }
}
