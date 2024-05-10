package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public Film create(Film film) {
        log.info("""
                Пользователь хочет добавить в список фильм: \
                Название - {}
                Описание - {}
                Продолжительность - {}
                Дата релиза - {}""", film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильму был присовен ID {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
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

    @Override
    public Film search(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Фильм с id = " + id + " не найден");
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public List<Film> getTopLikest(int count) {
        log.info("Выгрузка топа фильмов по лайкам с размером:{}", count);
        List<Film> filmList = new ArrayList<>(films.values());
        List<Film> sortedList = filmList
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
