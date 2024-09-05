package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id = 0L;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^\\S*$", message = "No whitespaces allowed")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public String getName() {
        if (name == null) {
            return login;
        }
        return name;
    }
}
