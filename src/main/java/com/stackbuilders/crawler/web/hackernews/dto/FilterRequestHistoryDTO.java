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
public class FilterRequestHistoryDTO {

    private Long id;

    @NotBlank(message = "El tipo de filtro no puede estar vacío")
    @Size(max = 255, message = "El tipo de filtro no puede tener más de 255 caracteres")
    private String filterType;

    @NotNull(message = "El timestamp de la solicitud no puede estar vacío")
    private LocalDateTime requestTimestamp;

    @NotBlank(message = "Las entradas devueltas no pueden estar vacías")
    @Size(min = 1, message = "Debe haber al menos una entrada")
    private String entriesReturned;
}
