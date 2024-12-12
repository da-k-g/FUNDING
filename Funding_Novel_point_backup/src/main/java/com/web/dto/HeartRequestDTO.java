package com.web.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartRequestDTO {

    private Long userId;
    private Long novelId;

    public HeartRequestDTO(Long userId, Long novelId) {
        this.userId = userId;
        this.novelId = novelId;
    }
    
}
