package com.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.domain.Like;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.domain.enums.VoteType;
import com.web.dto.LikeFormDTO;
import com.web.dto.ResponseVoteDTO;
import com.web.repository.LikeRepository;
import com.web.repository.NovelRepository;
import com.web.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * LikeService 인터페이스의 구현체
 * - 소설의 추천/비추천 상태를 관리하는 비즈니스 로직 처리
 * - 상태 업데이트, 추천 점수 계산, 추천 상태 조회를 포함
 */
@Service
@Transactional // 트랜젝션
@RequiredArgsConstructor // final 키워드가 붙은 모든 필드를 포함하는 생성자를 자동으로 생성
@Slf4j
public class LikeServiceImpl implements LikeService {

	
    private final LikeRepository likeRepository; // 좋아요 데이터 관리
    
    private final NovelRepository novelRepository; // 소설 데이터 관리
    
    private final UserRepository userRepository; // 유저 데이터 관리
    
    // 추천/비추천 상태 업데이트
    @Override
    public ResponseVoteDTO updateLike(LikeFormDTO likesFormDTO) {

        // 소설 엔티티 가져오기
        Novel novel = novelRepository.findById(likesFormDTO.getNovelId())
                .orElseThrow(() -> new EntityNotFoundException("소설을 찾을 수 없습니다."));
        // 소설 작성자 정보 가져오기
        User novelUser = userRepository.findById(novel.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("소설 작성자를 찾을 수 없습니다."));
        // 추천/비추천 요청한 사용자 정보 가져오기
        User user = userRepository.findById(likesFormDTO.getUid())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 이미 추천/비추천 기록이 있는지 확인
        Like existingLike = likeRepository.findByNovelAndUser(novel, user);

        String result = "";
        if (existingLike != null) {
            // 동일한 타입의 추천/비추천인 경우 삭제
           
        	if (existingLike.getVoteType().toString().equals(likesFormDTO.getVoteType())) {
                likeRepository.delete(existingLike);
                result =  "delete";
                
            } else {
                // 반대 타입으로 변경
                existingLike.setVoteType(VoteType.valueOf(likesFormDTO.getVoteType()));
                likeRepository.save(existingLike);
                result =  "update";
            }
            
        
            
        } else {
            // 새로운 추천/비추천 생성
            Like newLike = Like.builder()
                    .novel(novel)
                    .user(user)
                    .voteType(VoteType.valueOf(likesFormDTO.getVoteType()))
                    .novelWriter(novelUser.getId())
                    .build();
            likeRepository.save(newLike);
            
            
            result =  "create";
        }

        
        //추천/비추천 점수계산 저장
        novel.setLikeScore( calculateScore(novel.getId()));
        
        // 추천/비추천 수 조회 및 반환
        ResponseVoteDTO responseVoteDTO = likeRepository.countUpvotesMinusDownvotesPerNovelWithMin(novel.getId());       
        if (responseVoteDTO == null) {
            responseVoteDTO = new ResponseVoteDTO(novel.getId(), 0L, 0L);
        }
        responseVoteDTO.setMessage(result);
        return responseVoteDTO;
    }

    
    
    

    /**
     * 소설의 추천/비추천 점수 계산
     * @param novelId 소설 ID
     * @return 추천 점수
     */
    public int calculateScore(Long novelId) {
        // 추천 수
    	ResponseVoteDTO responseVoteDTO = likeRepository.countUpvotesMinusDownvotesPerNovelWithMin(novelId);
   
        // 추천 점수 계산
        return (int) (responseVoteDTO.getUpvoteCount() - responseVoteDTO.getDownvoteCount());
   }

    
    

    /**
     * 추천 비추천 정보
     * @param novelId
     * @param uid
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public ResponseVoteDTO getVoteState(Long novelId, Long uid) {
        ResponseVoteDTO responseVoteDTO = likeRepository.countUpvotesMinusDownvotesPerNovelWithMin(novelId);

        if(responseVoteDTO==null){
            //1. 등록된 정보가 없다면 기본값 설정후 반환
            responseVoteDTO = new ResponseVoteDTO();
            responseVoteDTO.setUpvoteCount(0L);
            responseVoteDTO.setDownvoteCount(0L);
        }else{
            if(uid==null){
                //2. 로그인 안한경우
                return responseVoteDTO;
            }
            //3. 로그인 되어 있다면 로그인한 유저가 추천, 또는 비추천을 했는지 확인
            User user = userRepository.findById(uid).orElseThrow(EntityNotFoundException::new);
            Novel novel = novelRepository.findById(novelId).orElseThrow(EntityNotFoundException::new);
            Like getLike= likeRepository.findByNovelAndUser(novel, user);
            if(getLike!=null){
                responseVoteDTO.setMyVoted(String.valueOf(getLike.getVoteType()));
            }
        }
        return  responseVoteDTO;
    }


}
