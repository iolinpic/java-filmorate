package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class FilmControllerTest {
    private static ObjectWriter objectWriter;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUpBeforeClass() {
        ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        objectWriter = objectMapper.writer();
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        this.mockMvc
                .perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void shouldReturnBadRequestOnEmptyUserCreateRequest() throws Exception {
        this.mockMvc.perform(post("/films"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnUserWithDefaultsCreateRequest() throws Exception {
        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnEmptyUserUpdateRequest() throws Exception {
        this.mockMvc.perform(put("/films"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnUserWithDefaultsUpdateRequest() throws Exception {
        this.mockMvc.perform(
                        put("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkOnValidUserCreateRequest() throws Exception {
        Film film = new Film();
        film.setName("123");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDescription("123@ms.sw");
        film.setDuration(2);
        film.setMpa(new Mpa(1L, ""));
        String json = objectWriter.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films")
                        .content(json).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(film.getName()));
    }

    @Test
    void shouldReturnNotFoundOnUpdateUserWithUnknownId() throws Exception {
        Film film = new Film();
        film.setName("123");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDescription("123@ms.sw");
        film.setDuration(2);
        String json = objectWriter.writeValueAsString(film);
        this.mockMvc.perform(
                        put("/films")
                                .content(json).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()));
    }

    @Test
    void shouldUpdateAfterCreation() throws Exception {
        Film film = new Film();
        film.setName("123");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDescription("123@ms.sw");
        film.setDuration(2);
        film.setMpa(new Mpa(1L, "123"));
        String json = objectWriter.writeValueAsString(film);
        this.mockMvc.perform(
                post("/films")
                        .content(json).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(film.getName()));
        film.setId(1L);
        film.setName("Updated-123");
        String json2 = objectWriter.writeValueAsString(film);
        this.mockMvc.perform(post("/films")
                        .content(json2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value(film.getName()));
    }
}