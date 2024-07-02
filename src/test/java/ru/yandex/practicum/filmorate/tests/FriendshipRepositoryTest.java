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
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmRepository.class, UserRepository.class, LikeRepository.class,
        FilmMpaaRepository.class, FilmGenreRepository.class, FriendshipRepository.class,
        GenreRepository.class, MpaRepository.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FriendshipRepositoryTest {
    private final FriendshipRepository friendshipRepository;

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void createFriendSHipTestOk() {
        final Friendship friendship = new Friendship(1, 2);

        friendshipRepository.save(friendship);

        final List<Friendship> friendshipList = friendshipRepository.findAll();
        assertEquals(1, friendshipList.size());
        assertThat(friendshipList.getFirst()).hasFieldOrPropertyWithValue("userId", 1L);
        assertThat(friendshipList.getFirst()).hasFieldOrPropertyWithValue("friendId", 2L);
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void findFriendshipTestOk() {
        final Friendship friendship = new Friendship(1, 2);
        final Friendship friendship1 = new Friendship(2, 3);
        final Friendship friendship2 = new Friendship(3, 4);

        friendshipRepository.save(friendship);
        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);

        Optional<Friendship> resultOrNot = friendshipRepository.findFriendship(2,3);
        assertTrue(resultOrNot.isPresent());
        Friendship result = resultOrNot.get();
        assertThat(result).hasFieldOrPropertyWithValue("userId", 2L);
        assertThat(result).hasFieldOrPropertyWithValue("friendId", 3L);
    }

    @Test
    @Sql(scripts = {"/test-get-users.sql"})
    void deleteFriendshipTestOk() {
        final Friendship friendship = new Friendship(1, 2);
        friendshipRepository.save(friendship);
        friendshipRepository.deleteFriendship(1L, 2L);
        assertEquals(0, friendshipRepository.findAll().size());
    }

}