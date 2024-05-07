package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService service;
    @Autowired
    private final InMemoryUserStorage storage;

    public UserController(UserService service, InMemoryUserStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        log.info("Пользователь выбрал отобразить список пользователей");
        return storage.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User create(@RequestBody User user) {
        return storage.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@RequestBody User newUser) {
        return storage.update(newUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/friends/{addId}")
    public User addFriend(@PathVariable("id") long id, @PathVariable("addId") long friendId) {
        return service.addFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{deleteId}")
    public User deleteFriend(@PathVariable("id") long id, @PathVariable("deleteId") long friendId) {
        return service.deleteFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable("id") long id) {
        return service.getFriendsList(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriend(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        return service.getMutualFriend(id, otherId);
    }
}

/* НУЖНО ДОБАВИТЬ:
1. Коды ответа
2. Вывод общих друзей у 2-х пользователей
3. Обработку ошибок через утили сборщик ошибок
 */
