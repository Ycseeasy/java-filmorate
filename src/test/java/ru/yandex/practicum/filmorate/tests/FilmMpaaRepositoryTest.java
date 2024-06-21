package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.FilmMpaaRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.FilmMpaa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmRepository.class, UserRepository.class, LikeRepository.class,
        FilmMpaaRepository.class, FilmMpaaRepository.class, FriendshipRepository.class,
        GenreRepository.class, MpaRepository.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FilmMpaaRepositoryTest {
    private final FilmMpaaRepository filmMpaaRepository;

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void changeMpaaTestOk() {
        final FilmMpaa filmMpaa = new FilmMpaa(1, 2);
        filmMpaaRepository.deleteMpaaFromFilm(1, 2);

        assertEquals(3, filmMpaaRepository.findAll().size());
        filmMpaaRepository.save(1, 3);
        final List<FilmMpaa> filmGenres = filmMpaaRepository.findAll();
        assertEquals(4, filmGenres.size());
        assertThat(filmGenres.getFirst()).hasFieldOrPropertyWithValue("filmId", 1L);
        assertThat(filmGenres.getFirst()).hasFieldOrPropertyWithValue("mpaaId", 3L);
    }
}