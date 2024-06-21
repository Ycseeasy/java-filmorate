package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        log.info("Пользователь выбрал отобразить список пользователей");
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User create(@RequestBody User user) {
        log.info("Пользователь выбрал добавления нового пользователя");
        validate(user);
        return service.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@RequestBody User newUser) {
        log.info("Пользователь выбрал обновить данные о пользователе");
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validate(newUser);
        return service.update(newUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/friends/{addId}")
    public User addFriend(@PathVariable("id") long id, @PathVariable("addId") long friendId) {
        log.info("Пользователь выбрал добавить друга");
        return service.addFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{deleteId}")
    public User deleteFriend(@PathVariable("id") long id, @PathVariable("deleteId") long friendId) {
        log.info("Пользователь выбрал удалить друга");
        return service.deleteFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable("id") long id) {
        log.info("Пользователь выбрал отобразить список друщей у пользователя");
        return service.getFriendsList(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends/common/{friendId}")
    public Collection<User> getCommonFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Пользователь выбрал поиск общих друщей у пользователей");
        return service.getMutualFriend(id, friendId);
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный формат email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин должен быть указан");
        }
        if (user.getLogin().length() != user.getLogin().trim().length()) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Вместо имени был использован логин - {}", user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
