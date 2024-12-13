package com.web.service;

import com.web.domain.Like;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.repository.LikeRepository;
import com.web.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private NovelRepository novelRepository;

 // 좋아요 추가 메서드 수정
    public void addLike(User user, Long novelId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel ID: " + novelId));

        Optional<Like> existingLike = likeRepository.findByUserAndNovel(user, novel);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("이미 누른좋아요."); // 메시지 명확히
        }

        Like like = new Like();
        like.setNovel(novel);
        like.setUser(user);
        likeRepository.save(like);
    }

    // 좋아요 삭제 메서드 수정
    public boolean removeLike(User user, Long novelId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel ID"));

        Optional<Like> existingLike = likeRepository.findByUserAndNovel(user, novel);
        if (existingLike.isEmpty()) {
            throw new IllegalStateException("이미 누른 싫어요."); // 메시지 명확히
        }

        likeRepository.delete(existingLike.get());
        return true;
    }


    public long getLikeCount(Long novelId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel ID: " + novelId));

        return likeRepository.countByNovel(novel);
    }
}