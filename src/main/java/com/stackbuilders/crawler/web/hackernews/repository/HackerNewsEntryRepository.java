package com.stackbuilders.crawler.web.hackernews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;

@Repository
public interface HackerNewsEntryRepository extends JpaRepository<HackerNewsEntry, Long> {
    // Métodos adicionales si se requieren, como filtros por atributos
}
