package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikesException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    @Autowired
    public InMemoryUserStorage storage;
    @Autowired
    public InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public List<User> whoLikes(Film film) {
        List<User> list = new ArrayList<>();
        for (long id : film.getLikes()) {
            list.add(storage.search(id));
        }
        return list;
    }

    public Film deleteLike(long filmId, long userId) {
        Film film = filmStorage.search(filmId);
        if (film.getLikes().remove(userId)) {
            log.info("Лайк пользователя с id - {} был успешно убран", userId);
        } else {
            throw new NotFoundException("Лайк пользователя с id - " + userId + " не был найден");
        }
        return film;
    }

    public Film addLike(long filmId, long userId) {
        Film film = filmStorage.search(filmId);
        storage.search(userId);
        if (film.getLikes().add(userId)) {
            log.info("Лайк пользователя с id - {} был успешно добавлен", userId);
        } else {
            throw new LikesException("Лайк с id - " + userId + " уже стоит");
        }
        return film;
    }

    public List<Film> getTopLikest(int count) {
        log.info("Выгрузка топа фильмов по лайкам с размером:{}", count);
        List<Film> sortedList = filmStorage.getAll()
                .stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film film1, Film film2) {
                        return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
                    }
                }).toList();

        return sortedList.subList(0, Math.min(count, sortedList.size()));
    }
}