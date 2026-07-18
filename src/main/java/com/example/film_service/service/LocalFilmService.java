package com.example.film_service.service;

import com.example.film_service.entity.Film;
import com.example.film_service.repository.FilmRepository;
import com.example.film_service.specification.FilmSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalFilmService {
    private final FilmRepository filmRepository;

    @Transactional(readOnly = true)
    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    @Transactional
    public Film addFilm(Film film) {
        return filmRepository.save(film);
    }

    @Transactional(readOnly = true)
    public Page<Film> searchFilms(
            String filmName,
            Integer year,
            Integer yearFrom,
            Integer yearTo,
            Double ratingFrom,
            Double ratingTo,
            Pageable pageable) {

        return filmRepository.findAll(
                FilmSpecification.filterBy(
                        filmName,
                        year,
                        yearFrom,
                        yearTo,
                        ratingFrom,
                        ratingTo),
                pageable
        );
    }

    @Transactional(readOnly = true)
    public List<Film> searchFilms(
            String filmName,
            Integer year,
            Integer yearFrom,
            Integer yearTo,
            Double ratingFrom,
            Double ratingTo
    ) {
        return filmRepository.findAll(
                FilmSpecification.filterBy(
                        filmName,
                        year,
                        yearFrom,
                        yearTo,
                        ratingFrom,
                        ratingTo)
        );
    }
}
