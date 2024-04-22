package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ControllerTest {
    static FilmController filmController = new FilmController();
    static UserController userController = new UserController();

    @Test
    void validateFilmOk() {
        final Film validFilm = new Film(
                "LOL8o0U1KDhgMkn",
                "ofqPRiyyYvHShP5xNP8iOHs5QzGO4dqwSdfZ1PeggkLav8zPqf",
                LocalDate.of(1970, 2, 20),
                67
        );
        filmController.validate(validFilm);
    }

    @Test
    void validateFilmFalse() {
        final Film validFilm2 = new Film(
                "LOL8o0U1KDhgMkn",
                "ofqPRiyyYvHShP5xNP8iOHs5QzGO4dqwSdfZ1PeggkLav8zPqf",
                LocalDate.of(1971, 2, 20),
                -102
        );
        Exception exception = assertThrows(
                ValidationException.class,
                () -> filmController.validate(validFilm2)
        );
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void validateUserOk() {
        final User user = new User(
                "bimbim@mail.ru",
                "Bimbim",
                "Ivan",
                LocalDate.of(1998, 9, 19)
        );
        userController.validate(user);
    }

    @Test
    void validateUserFalse() {
        final User user2 = new User(
                "bimbimmail.ru",
                "Bimbim",
                "Ivan",
                LocalDate.of(1998, 9, 19)
        );
        Exception exception = assertThrows(
                ValidationException.class,
                () -> userController.validate(user2)
        );
        assertEquals("Некорректный формат email", exception.getMessage());
    }
}

