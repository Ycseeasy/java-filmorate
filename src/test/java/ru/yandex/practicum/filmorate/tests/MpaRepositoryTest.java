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
import ru.yandex.practicum.filmorate.model.Mpa;


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
public class MpaRepositoryTest {
    private final MpaRepository mpaRepository;
    private final FilmMpaaRepository filmMpaaRepository;

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void findByIdMpaTestOk() {
        final Mpa mpa = mpaRepository.findById(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void findFilmMpaTestOk() {

        Optional<Mpa> mpa = mpaRepository.findByFilm(1);
        assertTrue(mpa.isPresent());
        Mpa result = mpa.get();
        assertThat(result).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    void findAllTestOk() {
        List<Mpa> mpaList = mpaRepository.findAllMpa();
        assertEquals(5, mpaList.size());

        assertThat(mpaList.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(mpaList.get(0)).hasFieldOrPropertyWithValue("name", "G");

        assertThat(mpaList.get(3)).hasFieldOrPropertyWithValue("id", 4L);
        assertThat(mpaList.get(3)).hasFieldOrPropertyWithValue("name", "R");
    }
}