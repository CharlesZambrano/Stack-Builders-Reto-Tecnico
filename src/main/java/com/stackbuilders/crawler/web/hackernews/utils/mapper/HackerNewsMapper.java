package com.stackbuilders.crawler.web.hackernews.utils.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.stackbuilders.crawler.web.hackernews.dto.HackerNewsEntryDTO;
import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;

@Mapper(componentModel = "spring")
public interface HackerNewsMapper {
    HackerNewsEntryDTO toDTO(HackerNewsEntry entry);

    HackerNewsEntry toEntity(HackerNewsEntryDTO dto);

    List<HackerNewsEntryDTO> toDTOs(List<HackerNewsEntry> entries);
}
