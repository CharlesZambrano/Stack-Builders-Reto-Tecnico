package com.stackbuilders.crawler.web.hackernews.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.stackbuilders.crawler.web.hackernews.model.FilterRequestHistory;
import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;
import com.stackbuilders.crawler.web.hackernews.repository.FilterRequestHistoryRepository;
import com.stackbuilders.crawler.web.hackernews.repository.HackerNewsEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FilterService {

        private final HackerNewsEntryRepository hackerNewsEntryRepository;
        private final FilterRequestHistoryRepository filterRequestHistoryRepository;

        public Page<HackerNewsEntry> filterWordsGreaterThanFive(int page, int size) {
                Page<HackerNewsEntry> result = hackerNewsEntryRepository
                                .getEntriesGreaterThanFiveWords(PageRequest.of(page, size));

                // Guardar el historial de la consulta
                filterRequestHistoryRepository.save(
                                FilterRequestHistory.builder()
                                                .filterType("Más de 5 palabras")
                                                .requestTimestamp(LocalDateTime.now())
                                                .entriesReturned(result.getContent().toString())
                                                .build());

                return result;
        }

        public Page<HackerNewsEntry> filterWordsLessThanOrEqualToFive(int page, int size) {
                Page<HackerNewsEntry> result = hackerNewsEntryRepository
                                .getEntriesLessThanOrEqualToFiveWords(PageRequest.of(page, size));

                // Guardar el historial de la consulta
                filterRequestHistoryRepository.save(
                                FilterRequestHistory.builder()
                                                .filterType("5 palabras o menos")
                                                .requestTimestamp(LocalDateTime.now())
                                                .entriesReturned(result.getContent().toString())
                                                .build());

                return result;
        }
}
