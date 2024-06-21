package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;

@RequestMapping("/films")
@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.info("Пользователь выбрал отобразить список фильмов");
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findById(@PathVariable("id") long id) {
        log.info("Пользователь выбрал найти фильм");
        return service.searchFilm(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film create(@RequestBody Film film) {
        log.info("Пользователь выбрал добавить фильм в список");
        validate(film);
        return service.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@RequestBody Film newFilm) {
        log.info("Пользователь выбрал обновить данные по фильму");
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validate(newFilm);
        return service.update(newFilm);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/like/{addId}")
    public Film addLike(@PathVariable("id") long id, @PathVariable("addId") long userId) {
        log.info("Пользователь выбрал поставить лайк фильму");
        return service.addLike(userId, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/like/{deleteId}")
    public Film deleteLike(@PathVariable("id") long id, @PathVariable("deleteId") long userId) {
        log.info("Пользователь выбрал убрать лайк с фильма");
        return service.deleteLike(userId, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Пользователь выбрал выгрузить популярные фильмы");
        if (count <= 0) {
            throw new ValidationException("Некорректный размер выборки. Размер должен быть больше нуля");
        }
        return service.getTopLikest(count);
    }

    private void validate(Film film) {
        LocalDate filmBirthday = LocalDate.of(1895, 12, 28);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
        if (film.getReleaseDate().isBefore(filmBirthday)) {
            throw new ValidationException("Дата релиза не может быть раньше даты создания кино");
        }
    }
}