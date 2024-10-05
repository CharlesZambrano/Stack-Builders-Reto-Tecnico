package com.stackbuilders.crawler.web.hackernews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;

@Repository
public interface HackerNewsEntryRepository extends JpaRepository<HackerNewsEntry, Long> {

    @Query(value = "SELECT * FROM get_news_entries_greater_than_five_words()", nativeQuery = true)
    Page<HackerNewsEntry> getEntriesGreaterThanFiveWords(Pageable pageable);

    @Query(value = "SELECT * FROM get_news_entries_less_than_or_equal_five_words()", nativeQuery = true)
    Page<HackerNewsEntry> getEntriesLessThanOrEqualToFiveWords(Pageable pageable);

    boolean existsByNumber(String number);
}
