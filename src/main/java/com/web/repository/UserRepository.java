package com.web.repository;



import com.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 사용자 이메일로 조회
    User findByEmail(String email);
}

