package com.example.film_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "films")
@Getter
@Setter
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "film_id", nullable = false)
    @NotNull(message = "filmId обязателен")
    private Long filmId;

    @Column(name = "film_name", nullable = false)
    @NotBlank(message = "Название фильма обязательно")
    private String filmName;

    @Column
    private Integer year;

    @Column
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String description;
}
