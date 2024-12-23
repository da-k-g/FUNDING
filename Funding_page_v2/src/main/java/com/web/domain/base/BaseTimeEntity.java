package com.web.domain.base;


/**
 * 엔티티의 생성 시간과 수정 시간을 자동으로 관리하는 상위 클래스
 */
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class})// 엔티티의 생성 및 수정 이벤트를 감지
@MappedSuperclass // 이 클래스를 상속받는 엔티티 클래스에 필드를 상속
@Getter
@Setter
public class BaseTimeEntity {

	 /**
     * 생성 시간
     * - 엔티티가 처음 저장될 때의 시간을 자동으로 기록
     */
	@CreatedDate
	@Column(updatable = false) // 생성시간 수정불가
	private LocalDateTime createDate;
	
	/**
     * 수정 시간
     * - 엔티티가 수정될 때의 시간을 자동으로 갱신
     */
    @LastModifiedDate // 수정 시간 자동 관리
	private LocalDateTime updateDate;
	
	
}
