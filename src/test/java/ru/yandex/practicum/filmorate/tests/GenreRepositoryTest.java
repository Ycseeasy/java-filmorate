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
import ru.yandex.practicum.filmorate.model.Genre;


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
public class GenreRepositoryTest {
    private final GenreRepository genreRepository;
    private final FilmGenreRepository filmGenreRepository;

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void findByIdGenreTestOk() {
        final Genre genre = genreRepository.findById(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void findFilmGenresTestOk() {
        filmGenreRepository.save(1,2);
        filmGenreRepository.save(1,3);
        filmGenreRepository.save(1,1);

        List<Genre> genreList = genreRepository.findFilmsGenres(1L);
        assertEquals(3, genreList.size());
        assertThat(genreList.getFirst()).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genreList.getFirst()).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void findAllTestOk() {
        List<Genre> genreList = genreRepository.findAllGenres();
        assertEquals(6, genreList.size());

        assertThat(genreList.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genreList.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");

        assertThat(genreList.get(3)).hasFieldOrPropertyWithValue("id", 4L);
        assertThat(genreList.get(3)).hasFieldOrPropertyWithValue("name", "Триллер");
    }
}