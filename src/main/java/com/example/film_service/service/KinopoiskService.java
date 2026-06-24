package com.example.film_service.service;

import com.example.film_service.dto.KinopoiskFilmDto;
import com.example.film_service.dto.KinopoiskSearchResponse;
import com.example.film_service.entity.Film;
import com.example.film_service.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KinopoiskService {

    private final RestTemplate restTemplate;
    private final FilmRepository filmRepository;

    private static final String API_URL = "https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword";

    @Value("${kinopoisk.api-key}")
    private String apiKey;

    @Transactional
    public List<Film> searchAndSaveFilms(String keyword, Integer page) {
        try {
            String url = API_URL + "?keyword=" + keyword;
            if (page != null) {
                url += "&page=" + page;
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("X-API-KEY", apiKey);

            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

            KinopoiskSearchResponse kinopoiskSearchResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    KinopoiskSearchResponse.class
            ).getBody();

            List<Film> savedFilms = new ArrayList<>();

            if (kinopoiskSearchResponse != null && kinopoiskSearchResponse.getFilms() != null) {
                for (KinopoiskFilmDto dto : kinopoiskSearchResponse.getFilms()) {
                    if (dto.getFilmId() != null && !filmRepository.existsByFilmId(dto.getFilmId())) {
                        Film film = new Film();
                        film.setFilmId(dto.getFilmId());
                        film.setFilmName(dto.getNameRu());
                        film.setYear(parseYear(dto.getYear()));
                        film.setRating(parseRating(dto.getRating()));
                        film.setDescription(dto.getDescription());
                        savedFilms.add(filmRepository.save(film));
                    }
                }
            }

            return savedFilms;
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "API Кинопоиска вернул ошибку: " + e.getStatusCode()
            );

        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Не удалось связаться с API Кинопоиска");
        }
    }

    private Integer parseYear(String year) {
        try {
            return year != null ? Integer.parseInt(year) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseRating(String rating) {
        try {
            return rating != null ? Double.parseDouble(rating) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
