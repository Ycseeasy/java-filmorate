package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.FilmMpaaRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmRepository.class, UserRepository.class, LikeRepository.class,
        FilmMpaaRepository.class, FilmGenreRepository.class, FriendshipRepository.class,
        GenreRepository.class, MpaRepository.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    void createUserTestOk() {
        final User user = generateCustomUser("user1@yandex.ru", "user1", "User1 Name",
                LocalDate.of(1990, 03, 24));

        userRepository.save(user);

        final List<User> users = userRepository.findAll();

        assertEquals(1, users.size());
        assertThat(users.getFirst()).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users.getFirst()).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(users.getFirst()).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(users.getFirst()).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(users.getFirst()).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void updateUserTestOk() {
        final User user = generateCustomUser("user3@yandex.ru", "user3", "User3 Name",
                LocalDate.of(1990, 03, 26));
        user.setId(1L);

        userRepository.update(user);

        final User updatedUser = userRepository.findById(1L);

        assertThat(updatedUser).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updatedUser).hasFieldOrPropertyWithValue("email", "user3@yandex.ru");
        assertThat(updatedUser).hasFieldOrPropertyWithValue("login", "user3");
        assertThat(updatedUser).hasFieldOrPropertyWithValue("name", "User3 Name");
        assertThat(updatedUser).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void getUserByIdTestOk() {
        User user = userRepository.findById(1L);

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(user).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(user).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(user).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void getAllUsersTestOk() {
        List<User> users = userRepository.findAll();

        assertEquals(4, users.size());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");

        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("email", "user2@yandex.ru");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("login", "user2");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("name", "User2 Name");
        assertThat(users.get(1)).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void deleteUsersTestTestOk() {
        userRepository.delete(1L);

        assertEquals(3, userRepository.findAll().size());
    }

    private User generateCustomUser(
            String email,
            String login,
            String name,
            LocalDate birthday) {
        return new User(email, login, name, birthday);
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void findByIdUserTestOk() {
        User user = userRepository.findById(1L);

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(user).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(user).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(user).hasFieldOrProperty("birthday");
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql", "/test-get-friends.sql"})
    void getFriendListTestOk() {

        List<User> friensList = userRepository.getFriendList(1);
        assertEquals(3, friensList.size());
        assertThat(friensList.getFirst()).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(friensList.getFirst()).hasFieldOrPropertyWithValue("email", "user2@yandex.ru");
        assertThat(friensList.getFirst()).hasFieldOrPropertyWithValue("login", "user2");
        assertThat(friensList.getFirst()).hasFieldOrPropertyWithValue("name", "User2 Name");
        assertThat(friensList.getFirst()).hasFieldOrProperty("birthday");
    }
}