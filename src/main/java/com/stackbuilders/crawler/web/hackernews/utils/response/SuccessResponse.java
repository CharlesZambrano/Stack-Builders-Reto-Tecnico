package com.stackbuilders.crawler.web.hackernews.utils.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private String method;
    private T data;
}
