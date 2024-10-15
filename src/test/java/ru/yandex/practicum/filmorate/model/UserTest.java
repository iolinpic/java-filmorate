package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class UserTest {
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
    public void shouldNotCreateEmptyUser() {
        User user = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotCreateUserWithEmptyOrInvalidEmail() {
        User user = new User();
        user.setName("asd");
        user.setLogin("asd");
        user.setBirthday(LocalDate.of(1999, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.stream().filter(c -> c.getPropertyPath().toString().equals("email")).count());
        user.setEmail("sad1Sda1@");
        violations = validator.validate(user);
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("email")).count());
    }

    @Test
    public void shouldNotCreateUserWithEmptyOrWhiteSpacedLogin() {
        User user = new User();
        user.setName("asd");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1999, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.stream().filter(c -> c.getPropertyPath().toString().equals("login")).count());
        user.setLogin("");
        violations = validator.validate(user);
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("login")).count());
        user.setLogin("sad1 Sda1@");
        violations = validator.validate(user);
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("login")).count());
    }

    @Test
    public void shouldNotCreateUserWithBirthDateInFuture() {
        User user = new User();
        user.setName("asd");
        user.setLogin("aasd");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(3125, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.stream().filter(c -> c.getPropertyPath().toString().equals("birthday")).count());
    }

    @Test
    public void shouldBeAbleToCreateUserWithEmptyNameAndPlaceALoginInstead() {
        User user = new User();
        user.setLogin("asd");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1125, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
        assertEquals(user.getLogin(), user.getName());
    }


}