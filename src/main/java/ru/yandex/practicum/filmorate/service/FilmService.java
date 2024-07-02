package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.FilmMpaaRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.LikesException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmBase;
    private final UserRepository userBase;
    private final LikeRepository likeBase;
    private final GenreRepository genreBase;
    private final MpaRepository mpaBase;
    private final FilmMpaaRepository filmMpaBase;
    private final FilmGenreRepository filmGenreBase;

    public Film deleteLike(long userId, long filmId) {
        log.info("произвоится удаление лайка пользователя с ID {} у фильма с ID {}", userId, filmId);
        Optional<Like> searchResult = likeBase.findLike(userId, filmId);
        if (searchResult.isPresent()) {
            if (likeBase.deleteLike(userId, filmId)) {
                log.info("лайк успешно удален");
                return filmBase.findById(filmId);
            } else {
                throw new InternalServerException("Не удалось удалить лайк");
            }
        } else {
            throw new LikesException("Лайк не найден");
        }
    }

    public Film addLike(long userId, long filmId) {
        log.info("производится добавление лайка фильму с ID {} пользователю с ID {}", filmId, userId);
        Film film = filmBase.findById(filmId);
        userBase.findById(userId);
        Optional<Like> searchResult = likeBase.findLike(userId, filmId);
        if (searchResult.isPresent()) {
            throw new LikesException("Фильму с ID - " + filmId
                    + " уже был проставлен лайк пользователем с ID - " + userId);
        }
        Like newLike = new Like(userId, filmId);
        likeBase.save(newLike);
        log.info("лайк успешно поставлен");
        return film;
    }

    public Film create(Film film) {
        log.info("""
                        Добавление в базу данных фильма:\s
                        Название - {}
                        Описание - {}
                        Дата выхода - {}
                        Длительность - {}
                        MPAA - {}
                        Жанры - {}
                        """, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa(), film.getGenres());
        Film savedFilm = filmBase.save(film);
        if (savedFilm.getMpa() != null) {
            if (film.getMpa().getId() > 5) {
                throw new ValidationException("ID Mpaa не может быть больше 5");
            }
            log.info("У фильма был найден MPAA - {}", savedFilm.getMpa());
            filmMpaBase.save(savedFilm.getId(), savedFilm.getMpa().getId());
        }

        if (film.getGenres() != null && !savedFilm.getGenres().isEmpty()) {
            HashSet<Long> idGenreList = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                if (genre.getId() > 6) {
                    throw new ValidationException("ID Жанра не может быть больше 6");
                }
                idGenreList.add(genre.getId());
            }
            log.info("У фильма был найден список жанров - {}", savedFilm.getGenres());
            for (long id : idGenreList) {
                filmGenreBase.save(film.getId(), id);
            }
        }
        return savedFilm;
    }

    public Film update(Film film) {
        log.info("""
                        Обновления полей фильма с ID - {}:\s
                        Название - {}
                        Описание - {}
                        Дата выхода - {}
                        Длительность - {}
                        MPAA - {}
                        Жанры - {}
                        """, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa(), film.getGenres());
        return filmBase.update(film);
    }

    public Film searchFilm(long id) {
        log.info("Поиск фильма с ID - {}", id);
        Film film = filmBase.findById(id);
        log.info("""
                        Был найден фильм с ID - {}:\s
                        Название - {}
                        Описание - {}
                        Дата выхода - {}
                        Длительность - {}
                        MPAA - {}
                        Жанры - {}
                        """, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa(), film.getGenres());
        List<Genre> genres = genreBase.findFilmsGenres(id);
        Optional<Mpa> mpa = mpaBase.findByFilm(id);
        film.setGenres(genres);
        mpa.ifPresent(film::setMpa);
        return film;
    }

    public List<Film> getAll() {
        return filmBase.findAll();
    }

    public List<Film> getTopLikest(int count) {
        return filmBase.getPopularFilms(count);
    }

    public List<Genre> getAllGenres() {
        return genreBase.findAllGenres();
    }

    public List<Mpa> getAllMpaa() {
        return mpaBase.findAllMpa();
    }

    public Genre getGenreById(long id) {
        log.info("Поиск жанра с ID - {}", id);
        if (genreBase.findById(id) == null) {
            throw new NotFoundException("Жанр с ID - " + id + " не найден");
        }
        return genreBase.findById(id);
    }

    public Mpa getMpaaById(long id) {
        log.info("Поиск MPAA с ID - {}", id);
        if (mpaBase.findById(id) == null) {
            throw new NotFoundException("Рейтинг c ID - " + id + " не найден");
        }
        return mpaBase.findById(id);
    }
}