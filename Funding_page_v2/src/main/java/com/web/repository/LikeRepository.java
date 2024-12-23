package com.web.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.domain.Like;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.domain.enums.VoteType;
import com.web.dto.NovelUpvoteCountDTO;
import com.web.dto.ResponseVoteDTO;

/**
 * Like 엔티티를 위한 Repository 인터페이스
 * - 추천 및 비추천 관련 데이터 관리
 */
public interface LikeRepository extends JpaRepository<Like, Long> {


	  /**
     * 소설별 추천/비추천 점수 합계를 계산하여 반환
     * - 추천: +1
     * - 비추천: -1
     * - 0인 소설은 제외
     * @param pageable 페이징 정보
     * @return NovelUpvoteCountDTO의 페이지 객체
     */
	@Query("SELECT new com.web.dto.NovelUpvoteCountDTO(l.novel.id, " +
		       "SUM(CASE WHEN l.voteType = com.web.domain.enums.VoteType.UP THEN 1 " +
		       "         WHEN l.voteType = com.web.domain.enums.VoteType.DOWN THEN -1 " +
		       "         ELSE 0 END)) " +
		       "FROM Like l " +
		       "GROUP BY l.novel.id " +
		       "HAVING SUM(CASE WHEN l.voteType = com.web.domain.enums.VoteType.UP THEN 1 " +
		       "               WHEN l.voteType = com.web.domain.enums.VoteType.DOWN THEN -1 " +
		       "               ELSE 0 END) <> 0")
		Page<NovelUpvoteCountDTO> countUpvotesMinusDownvotesPerNovelWithMin(Pageable pageable);



	   
    // 특정 소설과 사용자에 대한 추천/비추천 정보 조회
    
    Like findByNovelAndUser(Novel novel, User user);

   
    // 특정 소설과 사용자, 추천 유형에 대한 데이터 존재 여부 확인
    
    @Query("SELECT count(l) FROM Like l WHERE l.novel.id = :novelId AND l.user.id = :userId AND l.voteType = :voteType")
    Integer existsByNovelAndUserAndVoteType(@Param("novelId") Long novelId, @Param("userId") Long userId, @Param("voteType") VoteType voteType);

    

    // 특정 소설에 대한 추천 비추천 수 계산
    @Query("SELECT new com.web.dto.ResponseVoteDTO(l.novel.id, " +
            "SUM(CASE WHEN l.voteType = com.web.domain.enums.VoteType.UP THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN l.voteType = com.web.domain.enums.VoteType.DOWN THEN 1 ELSE 0 END)) " +
            "FROM Like l " +
            "GROUP BY l.novel.id " +
            "HAVING l.novel.id = :novelId")
    ResponseVoteDTO countUpvotesMinusDownvotesPerNovelWithMin(@Param("novelId") Long novelId);


    
  

}