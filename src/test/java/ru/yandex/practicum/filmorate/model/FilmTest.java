package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void afterAll() {
        validatorFactory.close();
    }

    @Test
    public void shouldFailValidationOnEmptyUser() {
        Film film = new Film();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotCreateFilmWithEmptyOrNullName() {
        Film film = new Film();
        film.setDuration(1);
        film.setDescription("alskd");
        film.setReleaseDate(LocalDate.now().minusYears(2));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.stream().filter(c -> c.getPropertyPath().toString().equals("name")).count());
        film.setName("");
        violations = validator.validate(film);
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("name")).count());
    }

    @Test
    public void shouldNotCreateFilmWithDescriptionLarger200() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("asd");
        film.setDescription("aaaaabbbbbcccccddddd".repeat(11));
        film.setReleaseDate(LocalDate.now().minusYears(2));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("description")).count());
    }

    @Test
    public void shouldNotCreateFilmWithNegativeDuration() {
        Film film = new Film();
        film.setDuration(-1);
        film.setName("asd");
        film.setDescription("aaaaabbbbbcccccddddd".repeat(11));
        film.setReleaseDate(LocalDate.now().minusYears(2));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("duration")).count());
    }

    @Test
    public void shouldNotCreateFilmWithReleaseDateBefore1885_12_28() {
        Film film = new Film();
        film.setDuration(1);
        film.setName("asd");
        film.setDescription("aaaaabbbbbcccccddddd");
        film.setReleaseDate(LocalDate.of(1884, 11, 28));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("releaseDate")).count());
    }
}