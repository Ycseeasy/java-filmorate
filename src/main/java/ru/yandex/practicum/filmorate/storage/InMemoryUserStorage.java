package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        log.info("""
                Пользователь хочет добавить в список пользователя: \
                email - {}
                Login - {}
                Имя - {}
                День рождения - {}""", user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователю был присовен ID {}", user.getId());
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("""
                        Пользователь хочет обновить данные пользователя на:\
                        ID - {}
                        email - {}
                        Login - {}
                        Имя - {}
                        День рождения - {}""",
                newUser.getId(), newUser.getEmail(), newUser.getLogin(), newUser.getName(), newUser.getBirthday());

        if (newUser.getId() == null) {
            log.error("Не указан ID");
            throw new ValidationException("Id должен быть указан");
        }
        validate(newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Данные пользователя {} успешно обновлены!", oldUser.getName());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public User search(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
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
            log.warn("Вместо имени был использован логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
