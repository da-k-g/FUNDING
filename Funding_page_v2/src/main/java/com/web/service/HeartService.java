package com.web.service;

import org.springframework.stereotype.Service;

import com.web.common.errors.exception.NotFoundException;
import com.web.domain.Heart;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.HeartRequestDTO;
import com.web.repository.HeartRepository;
import com.web.repository.NovelRepository;
import com.web.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


/**
 * 좋아요(Heart) 관련 비즈니스 로직 처리
 * - 사용자가 소설에 좋아요를 추가하거나 제거
 */
@Service
@RequiredArgsConstructor // final 키워드가 붙은 모든 필드를 포함하는 생성자를 자동으로 생성
public class HeartService {

    private final HeartRepository heartRepository; // 좋아요 데이터 관리
    private final UserRepository userRepository; // 사용자 데이터 관리
    private final NovelRepository novelRepository; // 소설 데이터 관리
    private final NovelService novelservice; // 소설 서비스

    // 좋아요 추가
    @Transactional // 클래스 또는 메서드 수준에서 트랜잭션 처리를 활성화
    public void insert(HeartRequestDTO heartRequestDTO) throws Exception {
    	// 사용자 조회
        User user = userRepository.findById(heartRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found user id : " + heartRequestDTO.getUserId()));
        // 소설 조회
        Novel novel = novelRepository.findById(heartRequestDTO.getNovelId())
                .orElseThrow(() -> new NotFoundException("Could not found novel id : " + heartRequestDTO.getNovelId()));

        // 이미 좋아요되어있으면 에러 반환
        if (heartRepository.findByUserAndNovel(user, novel).isPresent()){
            //TODO 409에러로 변경
            throw new Exception();
        }
        // 좋아요 생성 및 저장
        Heart heart = Heart.builder()
                .novel(novel)
                .user(user)
                .build();
        // 좋아요 수 증가
        heartRepository.save(heart);
      
    }

    // 좋아요 삭제
    @Transactional // 클래스 또는 메서드 수준에서 트랜잭션 처리를 활성화
    public void delete(HeartRequestDTO heartRequestDTO) {
    	// 사용자 조회
        User user = userRepository.findById(heartRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found user id : " + heartRequestDTO.getUserId()));
        // 소설 조회
        Novel novel = novelRepository.findById(heartRequestDTO.getNovelId())
                .orElseThrow(() -> new NotFoundException("Could not found novel id : " + heartRequestDTO.getNovelId()));
        // 좋아요 상태 확인
        Heart heart = heartRepository.findByUserAndNovel(user, novel)
                .orElseThrow(() -> new NotFoundException("Could not found heart id"));
        // 좋아요 삭제
        heartRepository.delete(heart);
       
    }
    
}