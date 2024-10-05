package com.stackbuilders.crawler.web.hackernews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;

@Repository
public interface HackerNewsEntryRepository extends JpaRepository<HackerNewsEntry, Long> {

    @Procedure(name = "HackerNewsEntry.getEntriesGreaterThanFiveWords")
    Page<HackerNewsEntry> getEntriesGreaterThanFiveWords(Pageable pageable);

    @Procedure(name = "HackerNewsEntry.getEntriesLessThanOrEqualToFiveWords")
    Page<HackerNewsEntry> getEntriesLessThanOrEqualToFiveWords(Pageable pageable);
}
