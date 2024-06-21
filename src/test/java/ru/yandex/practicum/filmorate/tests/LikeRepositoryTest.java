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
import ru.yandex.practicum.filmorate.model.Like;

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
public class LikeRepositoryTest {
    private final LikeRepository likeRepository;

    @Test
    @Sql(scripts = {"/test-get-films.sql", "/test-get-users.sql"})
    void createLikeTestOk() {
        final Like like = new Like(1, 2);

        likeRepository.save(like);

        final List<Like> likeList = likeRepository.findAll();
        assertEquals(1, likeList.size());
        assertThat(likeList.getFirst()).hasFieldOrPropertyWithValue("whoLikedId", 1L);
        assertThat(likeList.getFirst()).hasFieldOrPropertyWithValue("whatLikedId", 2L);
    }


    @Test
    @Sql(scripts = {"/test-get-films.sql", "/test-get-users.sql"})
    void findByIdLikeTestOk() {
        final Like like = new Like(1, 2);
        likeRepository.save(like);

        Optional<Like> result = likeRepository.findLike(1, 2);
        assertTrue(result.isPresent());
        Like like1 = result.get();
        assertThat(like1).hasFieldOrPropertyWithValue("whoLikedId", 1L);
        assertThat(like1).hasFieldOrPropertyWithValue("whatLikedId", 2L);

    }

    @Test
    @Sql(scripts = {"/test-get-films.sql", "/test-get-users.sql"})
    void deleteFilmGenreTest() {
        final Like like = new Like(1, 2);
        likeRepository.save(like);

        likeRepository.deleteLike(1,2);
        assertEquals(0, likeRepository.findAll().size());
    }
}