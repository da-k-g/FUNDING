package com.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.common.global.HttpResponseEntity.ResponseResult;
import static com.web.common.global.HttpResponseEntity.ResponseResult.success;
import com.web.dto.HeartRequestDTO;
import com.web.service.HeartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 좋아요(Heart) 관련 작업을 처리하는 컨트롤러
 */
@Slf4j // 로그 사용을 위한 Lombok 어노테이션
@RestController // RESTful API를 처리하는 컨트롤러임을 명시
@RequiredArgsConstructor // 생성자를 통한 의존성 주입
@RequestMapping("/heart") // "/heart"로 시작하는 요청을 처리
public class HeartController {

    private final HeartService heartService; // 좋아요 관련 서비스 클래스

    // 좋아요 추가 요청 처리
    @PostMapping
    public ResponseResult<?> insert(@RequestBody @Valid HeartRequestDTO heartRequestDTO) throws Exception {
    	// HeartRequestDTO를 기반으로 좋아요 추가
    	heartService.insert(heartRequestDTO);
        return success(); // 성공 응답 반환(HttpResponseEntity.java)
    }

    // 좋아요 삭체 요청 처리
    @DeleteMapping
    public ResponseResult<?> delete(@RequestBody @Valid HeartRequestDTO heartRequestDTO) {
    	   // HeartRequestDTO를 기반으로 좋아요 삭제
    	heartService.delete(heartRequestDTO);
        return success(); // 성공 응답 반환
    }
}
