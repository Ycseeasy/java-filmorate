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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

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
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;
    private final FilmMpaaRepository filmMpaaRepository;
    private final List<Genre> genres = List.of(new Genre(1L, "Комедия"), new Genre(2, "Драма"));
    private final Mpa mpa = new Mpa(1, "G");

    @Test
    void createFimTestOk() {
        final Film film = new Film("film", "about film",
                LocalDate.of(2009, 01, 01), 123, mpa, genres);

        filmRepository.save(film);
        filmMpaaRepository.save(film.getId(), mpa.getId());
        final List<Film> films = filmRepository.findAll();
        assertEquals(1, films.size());
        assertThat(films.getFirst()).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.getFirst()).hasFieldOrPropertyWithValue("name", "film");
        assertThat(films.getFirst()).hasFieldOrPropertyWithValue("description", "about film");
        assertThat(films.getFirst()).hasFieldOrProperty("releaseDate");
        assertThat(films.getFirst()).hasFieldOrProperty("duration");
        assertThat(films.getFirst()).hasFieldOrProperty("mpa");
        assertThat(films.getFirst()).hasFieldOrProperty("genres");
    }


    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void updateFilmTestOk() {
        final Film film = new Film("film", "about film", LocalDate.of(2009, 01, 01), 123);
        film.setId(1L);
        filmRepository.update(film);

        final Film updateFilm = filmRepository.findById(1L);

        assertThat(updateFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updateFilm).hasFieldOrPropertyWithValue("name", "film");
        assertThat(updateFilm).hasFieldOrPropertyWithValue("description", "about film");
        assertThat(updateFilm).hasFieldOrProperty("releaseDate");
        assertThat(updateFilm).hasFieldOrProperty("duration");
    }

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void getFilmByIdTestOk() {
        Film film = filmRepository.findById(1L);

        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Jhon Dig");
        assertThat(film).hasFieldOrPropertyWithValue("description", "Thirst Jhon Dig adventure");
        assertThat(film).hasFieldOrProperty("releaseDate");
        assertThat(film).hasFieldOrProperty("duration");
    }

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void getAllFilmsTestOk() {
        List<Film> films = filmRepository.findAll();

        assertEquals(4, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "Jhon Dig");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "Thirst Jhon Dig adventure");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrProperty("duration");

        assertThat(films.get(2)).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(films.get(2)).hasFieldOrPropertyWithValue("name", "Jhon Dig 3");
        assertThat(films.get(2)).hasFieldOrPropertyWithValue("description", "Thrid Jhon Dig adventure");
        assertThat(films.get(2)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(2)).hasFieldOrProperty("duration");
    }

    @Test
    @Sql(scripts = {"/test-get-films.sql"})
    void deleteFilmsTestOk() {
        filmMpaaRepository.deleteMpaaFromFilm(1L, 2L);
        filmRepository.delete(1L);
        assertEquals(3, filmRepository.findAll().size());
    }

    @Test
    @Sql(scripts = {"/test-get-films.sql", "/test-get-users.sql", "/test-get-likes.sql"})
    void getPopularFilmTestOk() {
        List<Film> popularFilms = filmRepository.getPopularFilms(3);
        assertEquals(3, popularFilms.size());
        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("name", "Jhon Dig 2");
        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("description", "Second Jhon Dig adventure");
        assertThat(popularFilms.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(popularFilms.get(0)).hasFieldOrProperty("duration");
    }
}