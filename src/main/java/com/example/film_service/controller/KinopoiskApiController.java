package com.example.film_service.controller;

import com.example.film_service.entity.Film;
import com.example.film_service.service.KinopoiskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/films")
@RequiredArgsConstructor
public class KinopoiskApiController {

    private final KinopoiskService kinopoiskService;

    @GetMapping
    public List<Film> searchAndSaveFilms(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer page) {
        return kinopoiskService.searchAndSaveFilms(keyword, page);
    }
}
