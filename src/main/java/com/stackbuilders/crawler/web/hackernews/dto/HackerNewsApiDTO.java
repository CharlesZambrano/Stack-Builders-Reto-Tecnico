package com.stackbuilders.crawler.web.hackernews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HackerNewsApiDTO {

    private Long id;
    private String title;
    private Integer descendants;
    private Integer score;
    private Long time;
}
