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
@Table(name = "hacker_news_entry")
public class HackerNewsEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El número de entrada no puede estar vacío")
    @Size(max = 10, message = "El número de entrada no puede tener más de 10 caracteres")
    @Column(name = "number", nullable = false)
    private String number;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 500, message = "El título no puede tener más de 500 caracteres")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Los puntos no pueden estar vacíos")
    @Column(name = "points", nullable = false)
    private Integer points;

    @NotNull(message = "El número de comentarios no puede estar vacío")
    @Column(name = "comments", nullable = false)
    private Integer comments;

    @NotNull(message = "El timestamp no puede estar vacío")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
