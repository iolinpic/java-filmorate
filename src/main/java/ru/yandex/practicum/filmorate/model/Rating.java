package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Rating {
    private Long id = 0L;
    @NotNull
    @NotBlank
    private String name;
}
