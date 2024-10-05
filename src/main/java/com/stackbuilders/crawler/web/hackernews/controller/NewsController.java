package com.stackbuilders.crawler.web.hackernews.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackbuilders.crawler.web.hackernews.model.HackerNewsEntry;
import com.stackbuilders.crawler.web.hackernews.service.CrawlerService;
import com.stackbuilders.crawler.web.hackernews.service.FilterService;
import com.stackbuilders.crawler.web.hackernews.utils.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "HackerNews", description = "Operaciones relacionadas con el scrapping de Hacker News")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.DELETE })
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

        private final CrawlerService crawlerService;
        private final FilterService filterService;

        @Operation(summary = "Iniciar scraping de Hacker News")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Scraping completado exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error en el proceso de scraping")
        })
        @PostMapping("/scrape")
        public ResponseEntity<SuccessResponse<Object>> scrapeNews() {
                crawlerService.scrapeAndStoreNewsEntries();
                SuccessResponse<Object> response = SuccessResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.OK.value())
                                .message("Scraping completado exitosamente")
                                .path("/api/v1/news/scrape")
                                .method("POST")
                                .data(null)
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Filtrar entradas con más de 5 palabras en el título")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Filtro aplicado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        @GetMapping("/filter/words-greater-than-five")
        public ResponseEntity<SuccessResponse<Page<HackerNewsEntry>>> filterWordsGreaterThanFive(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "30") int size) {

                Page<HackerNewsEntry> filteredEntries = filterService.filterWordsGreaterThanFive(page, size);
                SuccessResponse<Page<HackerNewsEntry>> response = SuccessResponse.<Page<HackerNewsEntry>>builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.OK.value())
                                .message("Filtro aplicado correctamente")
                                .path("/api/v1/news/filter/words-greater-than-five")
                                .method("GET")
                                .data(filteredEntries)
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Filtrar entradas con 5 palabras o menos en el título")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Filtro aplicado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        @GetMapping("/filter/words-less-or-equal-five")
        public ResponseEntity<SuccessResponse<Page<HackerNewsEntry>>> filterWordsLessThanOrEqualToFive(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "30") int size) {

                Page<HackerNewsEntry> filteredEntries = filterService.filterWordsLessThanOrEqualToFive(page, size);
                SuccessResponse<Page<HackerNewsEntry>> response = SuccessResponse.<Page<HackerNewsEntry>>builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.OK.value())
                                .message("Filtro aplicado correctamente")
                                .path("/api/v1/news/filter/words-less-or-equal-five")
                                .method("GET")
                                .data(filteredEntries)
                                .build();
                return ResponseEntity.ok(response);
        }
}
