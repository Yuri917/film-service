package com.example.film_service.specification;

import com.example.film_service.entity.Film;
import org.springframework.data.jpa.domain.Specification;

public class FilmSpecification {

    public static Specification<Film> filterBy(
            String filmName,
            Integer year,
            Integer yearFrom,
            Integer yearTo,
            Double ratingFrom,
            Double ratingTo) {

        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (filmName != null && !filmName.isBlank()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("filmName")),
                                "%" + filmName.toLowerCase() + "%"
                        ));
            }

            if (year != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("year"), year));
            }

            if (yearFrom != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("year"), yearFrom));
            }

            if (yearTo != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.lessThanOrEqualTo(root.get("year"), yearTo));
            }

            if (ratingFrom != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), ratingFrom));
            }

            if (ratingTo != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.lessThanOrEqualTo(root.get("rating"), ratingTo));
            }

            return predicates;
        };
    }
}
