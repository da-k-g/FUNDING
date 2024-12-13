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
	//좋아요 데이터를 관리하는 Repository
    @Autowired
    private LikeRepository likeRepository;
    //소설 데이터를 관리하는 Repository
    @Autowired
    private NovelRepository novelRepository;

 // 좋아요 추가 메서드 수정
    // 좋아요 추가
    public void addLike(User user, Long novelId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel ID: " + novelId));//소설이 존재하지 않으면 IllegalArgumentException 발생
        							
        								//LikeRepository를 사용하여 현재 사용자가 이미 좋아요를 눌렀는지 확인
        Optional<Like> existingLike = likeRepository.findByUserAndNovel(user, novel);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("이미 누른좋아요."); // 이미 좋아요를 누른 경우 IllegalStateException을 발생시켜 중복 처리를 방지
        }
        //좋아요 데이터 저장
        Like like = new Like();
        like.setNovel(novel);
        like.setUser(user);
        likeRepository.save(like);
    }

    // 좋아요 삭제 메서드 수정
    // 좋아요 삭제
    public boolean removeLike(User user, Long novelId) {
        Novel novel = novelRepository.findById(novelId)    //소설 ID로 소설을 조회
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel ID")); //소설이 존재하지 않으면 IllegalArgumentException 발생

        							//LikeRepository를 사용하여 현재 사용자가 이미 싫어요를 눌렀는지 확인
        Optional<Like> existingLike = likeRepository.findByUserAndNovel(user, novel);
        if (existingLike.isEmpty()) {
            throw new IllegalStateException("이미 누른 싫어요."); // 이미 싫어요를 누른 경우 IllegalStateException을 발생시켜 중복 처리를 방지
        }

        likeRepository.delete(existingLike.get());
        return true;
    }

    // 특정 소설의 좋아요 수 반환
    public long getLikeCount(Long novelId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel ID: " + novelId));
        						//소설이 존재하지 않으면 IllegalArgumentException 발생
        
        return likeRepository.countByNovel(novel);    // 좋아요 수 계산 
    }
}