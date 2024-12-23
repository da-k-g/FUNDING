package com.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.domain.Heart;
import com.web.domain.Novel;
import com.web.domain.User;
/**
 * Heart 엔티티를 위한 Repository 인터페이스
 * - 사용자와 소설 간의 좋아요 관계를 관리
 */
@Repository // Spring Repository로 선언
public interface HeartRepository extends JpaRepository<Heart, Long> {
	/**
    * 특정 사용자와 소설에 대한 좋아요 관계 조회
    * @param user 사용자 엔티티
    * @param novel 소설 엔티티
    * @return 해당 조건의 Heart 객체를 포함한 Optional
    */
	Optional<Heart> findByUserAndNovel(User user, Novel novel);
}
