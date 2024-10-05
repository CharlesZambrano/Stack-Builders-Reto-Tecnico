package com.stackbuilders.crawler.web.hackernews.utils.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.stackbuilders.crawler.web.hackernews.dto.FilterRequestHistoryDTO;
import com.stackbuilders.crawler.web.hackernews.model.FilterRequestHistory;

@Mapper(componentModel = "spring")
public interface FilterRequestHistoryMapper {
    FilterRequestHistoryDTO toDTO(FilterRequestHistory request);

    FilterRequestHistory toEntity(FilterRequestHistoryDTO dto);

    List<FilterRequestHistoryDTO> toDTOs(List<FilterRequestHistory> requests);
}
