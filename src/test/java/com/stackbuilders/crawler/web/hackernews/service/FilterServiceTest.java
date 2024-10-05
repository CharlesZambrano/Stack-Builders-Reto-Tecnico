package com.stackbuilders.crawler.web.hackernews.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;
import com.stackbuilders.crawler.web.hackernews.repository.FilterRequestHistoryRepository;
import com.stackbuilders.crawler.web.hackernews.repository.HackerNewsEntryRepository;

class FilterServiceTest {

    @Mock
    private HackerNewsEntryRepository hackerNewsEntryRepository;

    @Mock
    private FilterRequestHistoryRepository filterRequestHistoryRepository;

    @InjectMocks
    private FilterService filterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilterWordsGreaterThanFive() {
        Page<HackerNewsEntry> mockPage = new PageImpl<>(Collections.singletonList(new HackerNewsEntry()));
        when(hackerNewsEntryRepository.getEntriesGreaterThanFiveWords(any(PageRequest.class)))
                .thenReturn(mockPage);

        Page<HackerNewsEntry> result = filterService.filterWordsGreaterThanFive(0, 30);

        assertNotNull(result);
        verify(hackerNewsEntryRepository, times(1)).getEntriesGreaterThanFiveWords(any(PageRequest.class));
        verify(filterRequestHistoryRepository, times(1)).save(any());
    }

    @Test
    void testFilterWordsLessThanOrEqualToFive() {
        Page<HackerNewsEntry> mockPage = new PageImpl<>(Collections.singletonList(new HackerNewsEntry()));
        when(hackerNewsEntryRepository.getEntriesLessThanOrEqualToFiveWords(any(PageRequest.class)))
                .thenReturn(mockPage);

        Page<HackerNewsEntry> result = filterService.filterWordsLessThanOrEqualToFive(0, 30);

        assertNotNull(result);
        verify(hackerNewsEntryRepository, times(1)).getEntriesLessThanOrEqualToFiveWords(any(PageRequest.class));
        verify(filterRequestHistoryRepository, times(1)).save(any());
    }
}
