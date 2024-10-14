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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectWriter objectWriter;

    @BeforeAll
    static void setUpBeforeClass() {
        ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        objectWriter = objectMapper.writer();
    }


    @Test
    void shouldReturnEmptyUserList() throws Exception {
        this.mockMvc
                .perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void shouldReturnBadRequestOnEmptyUserCreateRequest() throws Exception {
        this.mockMvc.perform(post("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnUserWithDefaultsCreateRequest() throws Exception {
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnEmptyUserUpdateRequest() throws Exception {
        this.mockMvc.perform(put("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnUserWithDefaultsUpdateRequest() throws Exception {
        this.mockMvc.perform(
                        put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkOnValidUserCreateRequest() throws Exception {
        User user = new User();
        user.setLogin("123");
        user.setBirthday(LocalDate.now().minusYears(1));
        user.setEmail("123@ms.sw");
        String json = objectWriter.writeValueAsString(user);
        this.mockMvc.perform(
                post("/users")
                        .content(json).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(jsonPath("$.login").value(user.getLogin()));
    }

    @Test
    void shouldReturnNotFoundOnUpdateUserWithUnknownId() throws Exception {
        User user = new User();
        user.setLogin("123");
        user.setBirthday(LocalDate.now().minusYears(1));
        user.setEmail("123@ms.sw");
        String json = objectWriter.writeValueAsString(user);
        this.mockMvc.perform(
                        put("/users")
                                .content(json).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void shouldUpdateAfterCreation() throws Exception {
        User user = new User();
        user.setLogin("123");
        user.setBirthday(LocalDate.now().minusYears(1));
        user.setEmail("123@ms.sw");
        String json = objectWriter.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        user.setId(1L);
        user.setLogin("Updated-123");
        String json2 = objectWriter.writeValueAsString(user);
        this.mockMvc.perform(post("/users")
                        .content(json2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.login").value(user.getLogin()));
    }

}