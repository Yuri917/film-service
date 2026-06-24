package com.example.film_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
"films": [
    {
      "filmId": 263531,
      "nameRu": "Мстители",
      "nameEn": "The Avengers",
      "type": "FILM",
      "year": "2012",
      "description": "США, Джосс Уидон(фантастика)",
      "filmLength": "2:17",
      "countries": [
        {
          "country": "США"
        }
      ],
      "genres": [
        {
          "genre": "фантастика"
        }
      ],
      "rating": "NOTE!!! 7.9 for released film or 99% if film have not released yet",
      "ratingVoteCount": 284245,
      "posterUrl": "http://kinopoiskapiunofficial.tech/images/posters/kp/263531.jpg",
      "posterUrlPreview": "https://kinopoiskapiunofficial.tech/images/posters/kp_small/301.jpg"
    }

    "country" и "genres" нам не нужны: мы не сохраняем страны и жанры в БД, не фильтруем по ним,
    не выводим в отчётах. Jackson просто проигнорирует ключи JSON, для которых нет полей в Java-классе.
    Ошибки не будет. Это нормальная практика — не тащить в DTO лишние данные.
*/
public class KinopoiskFilmDto {
    private Long filmId;
    private String nameRu;
    private String nameEn;
    private String type;
    private String year;
    private String description;
    private String filmLength;
    private String rating;
    private Integer ratingVoteCount;
    private String posterUrl;
    private String posterUrlPreview;
}