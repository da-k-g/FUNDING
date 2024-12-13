package com.web.dto;

import lombok.Data;

@Data
public class ChapterDTO {
    private Long id;
    private Long novelId;
    private String title;
    private String content;
    private boolean isPaid;
}
