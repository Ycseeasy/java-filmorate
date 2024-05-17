package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikesException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film deleteLike(Film film, User user) {
        if (film.getLikes().remove(user.getId())) {
            log.info("Лайк пользователя с id - {} был успешно убран", user.getId());
        } else {
            throw new NotFoundException("Лайк пользователя с id - " + user.getId() + " не был найден");
        }
        return film;
    }

    public Film addLike(Film film, User user) {
        if (film.getLikes().add(user.getId())) {
            log.info("Лайк пользователя с id: {} был успешно добавлен", user.getId());
        } else {
            throw new LikesException("Лайк пользователя с id: " + user.getId() + " уже стоит");
        }
        return film;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film searchFilm(long id) {
        return filmStorage.search(id);
    }

    public User searchUser(long id) {
        return userStorage.search(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getTopLikest(int count) {
        return filmStorage.getTopLikest(count);
    }
}