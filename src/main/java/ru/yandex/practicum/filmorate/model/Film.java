package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private Set<Long> likes;
    private Genre genre;
    private AgeRating rating;

    public Film(String name, String description, LocalDate releaseDate, long duration, Genre genre, AgeRating rating) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genre = genre;
        this.rating = rating;
    }
}
