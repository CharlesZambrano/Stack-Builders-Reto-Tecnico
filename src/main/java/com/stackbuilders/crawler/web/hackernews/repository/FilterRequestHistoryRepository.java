package com.stackbuilders.crawler.web.hackernews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stackbuilders.crawler.web.hackernews.model.FilterRequestHistory;

@Repository
public interface FilterRequestHistoryRepository extends JpaRepository<FilterRequestHistory, Long> {

}
