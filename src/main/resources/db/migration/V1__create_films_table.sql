CREATE TABLE films
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    film_id     BIGINT       NOT NULL,
    film_name   VARCHAR(255) NOT NULL,
    year        INT,
    rating      DOUBLE,
    description TEXT
);