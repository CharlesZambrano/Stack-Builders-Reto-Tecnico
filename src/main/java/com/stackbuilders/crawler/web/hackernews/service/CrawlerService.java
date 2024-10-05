package com.stackbuilders.crawler.web.hackernews.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;
import com.stackbuilders.crawler.web.hackernews.repository.HackerNewsEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final HackerNewsEntryRepository hackerNewsEntryRepository;
    private final WebClient webClient = WebClient.create("https://hacker-news.firebaseio.com/v0");
    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    @Transactional
    public void scrapeAndStoreNewsEntries() {
        logger.info("Iniciando el proceso de scraping de Hacker News.");
        int batchSize = 30; // Tamaño del lote de historias a procesar
        int offset = 0; // Controla qué historias se están consultando

        // Intentar obtener 30 nuevas historias que no existan ya en la base de datos
        List<Integer> newStoryIds = findNewStoryIds(batchSize, offset);

        if (newStoryIds.isEmpty()) {
            logger.info("No se encontraron nuevas historias para guardar.");
            return;
        }

        // Procesar y guardar las nuevas historias
        newStoryIds.forEach(id -> {
            try {
                // Obtener detalles de la historia
                HackerNewsEntry entry = webClient.get()
                        .uri("/item/{id}.json", id)
                        .retrieve()
                        .bodyToMono(HackerNewsEntry.class)
                        .block();

                if (entry != null) {
                    logger.info("Procesando historia con ID: {}", id);

                    // Convertir Unix Time (time) a LocalDateTime (timestamp)
                    LocalDateTime createdTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(entry.getTime()),
                            ZoneOffset.UTC);
                    entry.setTimestamp(createdTime); // Asignar el tiempo convertido

                    // Completar campos
                    entry.setNumber(id.toString());
                    entry.setComments(entry.getComments() != null ? entry.getComments() : 0);
                    entry.setPoints(entry.getPoints() != null ? entry.getPoints() : 0);
                    entry.setTitle(entry.getTitle() != null ? entry.getTitle() : "Sin título");

                    // Guardar la entrada
                    hackerNewsEntryRepository.save(entry);
                    logger.info("Entrada guardada en la base de datos: {}", entry);
                } else {
                    logger.warn("No se pudo obtener detalles para la historia con ID: {}", id);
                }
            } catch (Exception e) {
                logger.error("Error al procesar la historia con ID: {}", id, e);
            }
        });

        logger.info("Finalizó el proceso de scraping y almacenamiento.");
    }

    private List<Integer> findNewStoryIds(int batchSize, int offset) {
        int attempts = 0; // Controla cuántas veces se han intentado obtener nuevas historias
        List<Integer> newStoryIds;

        do {
            // Obtener un lote de 30 historias (paginar con offset)
            List<Integer> topStoryIds = getTopStoryIds(offset, batchSize);

            // Filtrar las que ya existen en la base de datos
            newStoryIds = topStoryIds.stream()
                    .filter(id -> !hackerNewsEntryRepository.existsByNumber(id.toString()))
                    .collect(Collectors.toList());

            if (newStoryIds.isEmpty()) {
                // Si todas ya existen, aumentar el offset para buscar las siguientes 30
                // historias
                offset += batchSize;
                attempts++;
                logger.info("Todas las historias en este lote ya existen. Intentando con el siguiente lote de 30.");
            }

        } while (newStoryIds.isEmpty() && attempts < 5); // Limitar a 5 intentos para evitar loops infinitos

        return newStoryIds;
    }

    private List<Integer> getTopStoryIds(int offset, int limit) {
        // Obtener los IDs de las historias principales y aplicar paginación interna
        List<Integer> allTopStoryIds = webClient.get()
                .uri("/topstories.json?print=pretty")
                .retrieve()
                .bodyToFlux(Integer.class)
                .collectList()
                .block();

        if (allTopStoryIds == null || allTopStoryIds.isEmpty()) {
            logger.error("No se obtuvieron IDs de historias de Hacker News.");
            return List.of(); // Devolver lista vacía si no se obtuvieron IDs
        }

        // Aplicar paginación interna
        return allTopStoryIds.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
