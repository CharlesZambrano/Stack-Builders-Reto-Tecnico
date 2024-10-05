package com.stackbuilders.crawler.web.hackernews.service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void scrapeAndStoreNewsEntries() {
        List<Integer> topStoryIds = webClient.get()
                .uri("/topstories.json?print=pretty")
                .retrieve()
                .bodyToFlux(Integer.class)
                .take(30)
                .collectList()
                .block();

        topStoryIds.forEach(id -> {
            HackerNewsEntry entry = webClient.get()
                    .uri("/item/{id}.json", id)
                    .retrieve()
                    .bodyToMono(HackerNewsEntry.class)
                    .block();

            if (entry != null) {
                entry.setTimestamp(LocalDateTime.now());
                hackerNewsEntryRepository.save(entry);
            }
        });
    }
}
