package com.stackbuilders.crawler.web.hackernews.utils.response;

import java.time.LocalDateTime;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class SuccessResponseFilter implements WebFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).then(Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            if (response.getStatusCode() != null && response.getStatusCode().is2xxSuccessful()) {
                // Crear objeto de respuesta exitosa
                SuccessResponse<Object> successResponse = SuccessResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(response.getStatusCode().value())
                        .message("Operación exitosa")
                        .path(exchange.getRequest().getURI().getPath())
                        .method(exchange.getRequest().getMethod().name())
                        .build();

                // Convertir el objeto successResponse a JSON
                byte[] bytes;
                try {
                    bytes = objectMapper.writeValueAsBytes(successResponse); // Convertir a JSON
                } catch (Exception e) {
                    return Mono.error(e); // Manejar cualquier error de conversión
                }

                // Establecer el contenido como JSON y enviar la respuesta
                response.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                return response.writeWith(Mono.just(buffer));
            }
            return Mono.empty();
        }));
    }
}
