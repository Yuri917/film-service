package com.example.film_service.controller;

import com.example.film_service.entity.Film;
import com.example.film_service.service.EmailService;
import com.example.film_service.service.LocalFilmService;
import com.example.film_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/films")
@RequiredArgsConstructor
public class LocalFilmController {

    private final LocalFilmService localFilmService;
    private final ReportService reportService;
    private final EmailService emailService;

    @GetMapping
    public List<Film> getAllFilms() {
        return localFilmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return localFilmService.addFilm(film);
    }

    @GetMapping("/search")
    public Page<Film> searchFilms(
            @RequestParam(required = false) String filmName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Double ratingFrom,
            @RequestParam(required = false) Double ratingTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Sort sortOrder = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        return localFilmService.searchFilms(
                filmName,
                year,
                yearFrom,
                yearTo,
                ratingFrom,
                ratingTo,
                pageable);
    }

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "filmId", "filmName", "year", "rating", "description");

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",");
        String field = parts[0];

        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недопустимое поле сортировки: " + field
            );
        }

        Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, field);
    }

    @GetMapping("/report")
    public ResponseEntity<String> getReport(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) String filmName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Double ratingFrom,
            @RequestParam(required = false) Double ratingTo) {

        List<Film> films = localFilmService.searchFilms(
                filmName,
                year,
                yearFrom,
                yearTo,
                ratingFrom,
                ratingTo);

        HttpHeaders headers = new HttpHeaders();

        if ("xml".equalsIgnoreCase(format)) {
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.set("Content-Disposition", "attachment; filename=films.xml");
            return ResponseEntity.ok().headers(headers).body(reportService.generateXml(films));
        } else {
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.set("Content-Disposition", "attachment; filename=films.csv");
            return ResponseEntity.ok().headers(headers).body(reportService.generateCsv(films));
        }
    }

    @PostMapping("/report/send")
    public ResponseEntity<String> sendReport(
            @RequestParam String email,
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) String filmName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Double ratingFrom,
            @RequestParam(required = false) Double ratingTo) {

        List<Film> films = localFilmService.searchFilms(
                filmName,
                year,
                yearFrom,
                yearTo,
                ratingFrom,
                ratingTo);

        String report;
        String attachmentName;

        if ("xml".equalsIgnoreCase(format)) {
            report = reportService.generateXml(films);
            attachmentName = "films.xml";
        } else {
            report = reportService.generateCsv(films);
            attachmentName = "films.csv";
        }

        emailService.sendReport(
                email,
                "Отчёт по фильмам",
                "Во вложении отчёт по фильмам в формате " + format.toUpperCase(),
                attachmentName,
                report.getBytes(StandardCharsets.UTF_8)
        );

        return ResponseEntity.ok("Отчёт отправлен на " + email);
    }
}
