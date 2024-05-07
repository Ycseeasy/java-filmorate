package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    @Autowired
    private final FilmService service;
    @Autowired
    private final InMemoryFilmStorage storage;

    public FilmController(FilmService service, InMemoryFilmStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.info("Пользователь выбрал отобразить список фильмов");
        return storage.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film create(@RequestBody Film film) {
        return storage.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@RequestBody Film newFilm) {
        return storage.update(newFilm);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/like/{addId}")
    public Film addLike(@PathVariable("id") long id, @PathVariable("addId") long userId) {
        return service.addLike(id, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/like/{deleteId}")
    public Film deleteLike(@PathVariable("id") long id, @PathVariable("deleteId") long userId) {
        return service.deleteLike(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) {
            throw new ValidationException("Некорректный размер выборки. Размер должен быть больше нуля");
        }
        return service.getTopLikest(count);
    }
}

/* НУЖНО ДОБАВИТЬ:
1. Коды ответа
2. Вывод списка всех фильмов по рейтингу (Лайков)
3. Обработку ошибок через утили сборщик ошибок
 */