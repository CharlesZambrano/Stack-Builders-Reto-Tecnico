package com.stackbuilders.crawler.web.hackernews.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HackerNewsEntryDTO {

    private Long id;

    @NotBlank(message = "El número de entrada no puede estar vacío")
    @Size(max = 10, message = "El número de entrada no puede tener más de 10 caracteres")
    private String number;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 500, message = "El título no puede tener más de 500 caracteres")
    private String title;

    @NotNull(message = "Los puntos no pueden estar vacíos")
    private Integer points;

    @NotNull(message = "El número de comentarios no puede estar vacío")
    private Integer comments;

    @NotNull(message = "El timestamp no puede estar vacío")
    private LocalDateTime timestamp;
}
