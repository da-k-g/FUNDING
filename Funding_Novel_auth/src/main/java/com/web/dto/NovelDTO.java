package com.web.dto;

import lombok.Data;

@Data
public class NovelDTO {
    private Long id;
    private String title;
    private String author;
    private String description;
    private String category;
    private boolean isPaid;
}

