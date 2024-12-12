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

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final NovelRepository novelRepository;
    private final NovelService novelservice;

    @Transactional
    public void insert(HeartRequestDTO heartRequestDTO) throws Exception {

        User user = userRepository.findById(heartRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found user id : " + heartRequestDTO.getUserId()));

        Novel novel = novelRepository.findById(heartRequestDTO.getNovelId())
                .orElseThrow(() -> new NotFoundException("Could not found novel id : " + heartRequestDTO.getNovelId()));

        // 이미 좋아요되어있으면 에러 반환
        if (heartRepository.findByUserAndNovel(user, novel).isPresent()){
            //TODO 409에러로 변경
            throw new Exception();
        }

        Heart heart = Heart.builder()
                .novel(novel)
                .user(user)
                .build();

        heartRepository.save(heart);
        novelservice.addLikeCount(null);
    }

    @Transactional
    public void delete(HeartRequestDTO heartRequestDTO) {

        User user = userRepository.findById(heartRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found user id : " + heartRequestDTO.getUserId()));

        Novel novel = novelRepository.findById(heartRequestDTO.getNovelId())
                .orElseThrow(() -> new NotFoundException("Could not found novel id : " + heartRequestDTO.getNovelId()));

        Heart heart = heartRepository.findByUserAndNovel(user, novel)
                .orElseThrow(() -> new NotFoundException("Could not found heart id"));

        heartRepository.delete(heart);
        novelservice.subLikeCount(null);
    }
}