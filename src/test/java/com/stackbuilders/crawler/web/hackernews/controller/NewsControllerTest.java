package com.stackbuilders.crawler.web.hackernews.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;
import com.stackbuilders.crawler.web.hackernews.repository.HackerNewsEntryRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HackerNewsEntryRepository hackerNewsEntryRepository;

    @BeforeEach
    void setUp() {
        hackerNewsEntryRepository.deleteAll();

        HackerNewsEntry entry1 = new HackerNewsEntry();
        entry1.setNumber("123");
        entry1.setTitle("Una entrada con muchas palabras en el título");
        entry1.setComments(10);
        entry1.setPoints(20);
        entry1.setTimestamp(LocalDateTime.now());
        hackerNewsEntryRepository.save(entry1);

        HackerNewsEntry entry2 = new HackerNewsEntry();
        entry2.setNumber("456");
        entry2.setTitle("Cinco palabras en total");
        entry2.setComments(5);
        entry2.setPoints(10);
        entry2.setTimestamp(LocalDateTime.now());
        hackerNewsEntryRepository.save(entry2);
    }

    @Test
    void testScrapeNewsEndpoint() throws Exception {
        mockMvc.perform(post("/api/v1/news/scrape"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Scraping completado exitosamente"))
                .andExpect(jsonPath("$.status").value(200));
    }
}
