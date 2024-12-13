package com.web.repository;
import com.web.domain.QnA;
import com.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Long> {
    List<QnA> findAllByAuthor(User author);
}
