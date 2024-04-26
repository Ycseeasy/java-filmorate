package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    LocalDate filmBirthday = LocalDate.of(1895, 12, 28);

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Пользователь выбрал отобразить список фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("""
                Пользователь хочет добавить в список фильм: \
                Название - {}
                Описание - {}
                Продолжительность - {}
                Дата релиза - {}""", film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильму был присовен ID {}", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("""
                Пользователь выбрал обновить фильм из списока\
                Фильм для обновления:
                ID - {}
                Название - {}
                Описание - {}
                Продолжительность - {}
                Дата релиза - {}""", newFilm.getId(), newFilm.getName(), newFilm.getDescription(),
                newFilm.getDuration(), newFilm.getReleaseDate());

        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validate(newFilm);
        if (films.containsKey(newFilm.getId())) {
            Film oldFim = films.get(newFilm.getId());
            oldFim.setName(newFilm.getName());
            oldFim.setDuration(newFilm.getDuration());
            oldFim.setDescription(newFilm.getDescription());
            oldFim.setReleaseDate(newFilm.getReleaseDate());
            log.info("Фильм {} успешно обновлен!", oldFim.getName());
            return oldFim;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private void validate(Film film) {
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
