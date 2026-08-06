package com.example.film_service.service;

import com.example.film_service.entity.Film;
import com.example.film_service.repository.FilmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalFilmServiceTest {

    @Mock
    private FilmRepository filmRepository;
    //private final FilmRepository filmRepository = mock(FilmRepository.class);

    @InjectMocks
    private LocalFilmService localFilmService;
    //LocalFilmService localFilmService = new LocalFilmService(filmRepository);

    @Test
    void shouldCallRepositoryFindAll() {
        localFilmService.getFilms();
        verify(filmRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnFilmsFromRepository() {
        // 1. Подготавливаем данные
        Film film = new Film();
        film.setId(1L);
        film.setFilmId(101L);
        film.setFilmName("Тестовый фильм");
        film.setYear(2023);
        film.setRating(7.5);
        film.setDescription("Описание фильма");

        //List<Film> filmList = List.of(film);
        // Создаём объект Page. PageImpl — простая реализация интерфейса Page.
        Page<Film> expectedPage = new PageImpl<>(List.of(film));

        // 2. Настраиваем мок
        when(filmRepository.findAll(
                any(Specification.class), // любая спецификация
                any(Pageable.class)       // любой Pageable
        )).thenReturn(expectedPage);

        // 3. Вызываем тестируемый метод с ЛЮБЫМИ параметрами
        // (можно передать все null, чтобы проверить базовый случай)
        Page<Film> actualPage = localFilmService.searchFilms(
                null, null, null, null, null, null,
                PageRequest.of(0, 20)      // пагинация
        );

        // 4. Проверяем, что результат совпадает
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void shouldReturnEmptyPage() {
        Page<Film> expectedPage = new PageImpl<>(Collections.emptyList());

        when(filmRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(expectedPage);

        Page<Film> actualPage = localFilmService.searchFilms(
                null, null, null, null, null, null,
                PageRequest.of(0, 20)      // пагинация
        );

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void shouldReturnFilmFromRepository() {
        Film film = new Film();
    }
}
