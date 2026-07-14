package com.example.film_service.service;

import com.example.film_service.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalFilmService {
    private final FilmRepository filmRepository;


}
