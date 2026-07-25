package com.example.film_service.service;

import com.example.film_service.entity.Film;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    // private final XmlMapper xmlMapper = (XmlMapper) new XmlMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private final XmlMapper xmlMapper;

    // Конструктор для инициализации XmlMapper с нужными настройками
    public ReportService() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String generateCsv(List<Film> films) {
        StringBuilder sb = new StringBuilder();
        sb.append("\uFEFF"); // BOM для корректного отображения кириллицы в Excel
        sb.append("id,filmId,filmName,year,rating,description\n");
        for (Film film : films) {
            sb.append(film.getId()).append(",");
            sb.append(film.getFilmId()).append(",");
            sb.append(escapeCsv(film.getFilmName())).append(",");
            sb.append(film.getYear() != null ? film.getYear() : "").append(",");
            sb.append(film.getRating() != null ? film.getRating() : "").append(",");
            sb.append(escapeCsv(film.getDescription())).append("\n");
        }

        return sb.toString();
    }

    public String generateXml(List<Film> films) {
        try {
            return xmlMapper.writeValueAsString(films);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при генерации XML", e);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");
        if (!needsQuotes) {
            return value;
        }

        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (char c : value.toCharArray()) {
            if (c == '"') sb.append('"'); // удваиваем кавычку
            sb.append(c);
        }
        sb.append('"');
        return sb.toString();
    }
}