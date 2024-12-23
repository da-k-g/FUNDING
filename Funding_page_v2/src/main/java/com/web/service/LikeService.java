package com.web.service;

import com.web.dto.LikeFormDTO;
import com.web.dto.ResponseVoteDTO;
/**
 * 좋아요 및 비추천 관련 서비스 인터페이스
 * - 소설에 대한 좋아요(추천) 및 비추천 상태를 업데이트하거나 조회
 */
public interface LikeService {
	// 추천/비추천 상태 업데이트 
	public ResponseVoteDTO updateLike(LikeFormDTO likesFormDTO);
	// 특정 소설에 대한 추천/ 비추천 상태 조회
	// - 사용자의 현채 추천/비추천 상태와 총 추천/비추천 수를 반환
	public ResponseVoteDTO getVoteState(Long novelId, Long uid);

}
