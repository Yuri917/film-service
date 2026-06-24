package com.example.film_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
/*
JSON, полученный от API кинопоиска, созданный по структуре ответа:
{
  "keyword": "мстители",
  "pagesCount": 7,
  "searchFilmsCountResult": 134,
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
  ]
}
 */
public class KinopoiskSearchResponse {
    private String keyword;
    private Integer pagesCount;
    private Integer searchFilmsCountResult;
    private List<KinopoiskFilmDto> films;
}
