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

import com.stackbuilders.crawler.web.hackernews.dto.HackerNewsApiDTO;
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
        int batchSize = 30;
        int offset = 0;

        List<Integer> newStoryIds = findNewStoryIds(batchSize, offset);

        if (newStoryIds.isEmpty()) {
            logger.info("No se encontraron nuevas historias para guardar.");
            return;
        }

        newStoryIds.forEach(id -> {
            try {
                HackerNewsApiDTO apiEntry = webClient.get()
                        .uri("/item/{id}.json", id)
                        .retrieve()
                        .bodyToMono(HackerNewsApiDTO.class)
                        .block();

                if (apiEntry != null) {
                    logger.info("Procesando historia con ID: {}", id);

                    HackerNewsEntry entry = new HackerNewsEntry();
                    entry.setNumber(apiEntry.getId().toString());
                    entry.setTitle(apiEntry.getTitle() != null ? apiEntry.getTitle() : "Sin título");

                    entry.setComments(apiEntry.getDescendants() != null ? apiEntry.getDescendants() : 0);

                    entry.setPoints(apiEntry.getScore() != null ? apiEntry.getScore() : 0);

                    LocalDateTime createdTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(apiEntry.getTime()),
                            ZoneOffset.UTC);
                    entry.setTimestamp(createdTime);

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
        int attempts = 0;
        List<Integer> newStoryIds;

        do {
            List<Integer> topStoryIds = getTopStoryIds(offset, batchSize);

            newStoryIds = topStoryIds.stream()
                    .filter(id -> !hackerNewsEntryRepository.existsByNumber(id.toString()))
                    .collect(Collectors.toList());

            if (newStoryIds.isEmpty()) {
                offset += batchSize;
                attempts++;
                logger.info("Todas las historias en este lote ya existen. Intentando con el siguiente lote de 30.");
            }

        } while (newStoryIds.isEmpty() && attempts < 5);

        return newStoryIds;
    }

    private List<Integer> getTopStoryIds(int offset, int limit) {
        List<Integer> allTopStoryIds = webClient.get()
                .uri("/topstories.json?print=pretty")
                .retrieve()
                .bodyToFlux(Integer.class)
                .collectList()
                .block();

        if (allTopStoryIds == null || allTopStoryIds.isEmpty()) {
            logger.error("No se obtuvieron IDs de historias de Hacker News.");
            return List.of();
        }

        return allTopStoryIds.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
