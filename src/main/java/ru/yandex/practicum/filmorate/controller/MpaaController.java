package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RequestMapping("/mpa")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MpaaController {

    private final FilmService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Mpa> findAll() {
        log.info("Пользователь выбрал отобразить все рейтинги");
        return service.getAllMpaa();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa findById(@PathVariable("id") long id) {
        log.info("Пользователь выбрал поиск рейтинга по id");
        return service.getMpaaById(id);
    }
}
