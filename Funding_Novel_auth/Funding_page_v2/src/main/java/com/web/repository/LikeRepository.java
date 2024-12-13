package com.web.repository;

import com.web.domain.Like;
import com.web.domain.Novel;
import com.web.domain.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {

    // 사용자와 소설로 특정 좋아요를 검색
    Optional<Like> findByUserAndNovel(User user, Novel novel);

    // 특정 소설의 좋아요 수를 카운트
    Long countByNovel(Novel novel);

    // 좋아요 수가 많은 순서대로 소설 목록을 검색
    @Query("SELECT n FROM Novel n LEFT JOIN Like l ON n.id = l.novel.id " +
           "GROUP BY n.id " +
           "ORDER BY COUNT(DISTINCT l.id) DESC") // DISTINCT 추가로 중복 데이터 방지
    List<Novel> findNovelsByLikes();

    // 중복 좋아요 확인 쿼리 (Optional 추가)
    @Query("SELECT COUNT(l) FROM Like l WHERE l.user.id = :userId AND l.novel.id = :novelId")
    Long findDuplicateLikeCount(Long userId, Long novelId);
}
