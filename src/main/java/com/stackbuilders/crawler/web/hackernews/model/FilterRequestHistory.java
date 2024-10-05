package com.stackbuilders.crawler.web.hackernews.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Builder
@Table(name = "filter_request_history")
public class FilterRequestHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El tipo de filtro no puede estar vacío")
    @Size(max = 255, message = "El tipo de filtro no puede tener más de 255 caracteres")
    @Column(name = "filter_type", nullable = false)
    private String filterType;

    @NotNull(message = "El timestamp de la solicitud no puede estar vacío")
    @Column(name = "request_timestamp", nullable = false)
    private LocalDateTime requestTimestamp;

    @NotBlank(message = "Las entradas devueltas no pueden estar vacías")
    @Size(min = 1, message = "Debe haber al menos una entrada")
    @Column(name = "entries_returned", columnDefinition = "TEXT", nullable = false)
    private String entriesReturned;
}
