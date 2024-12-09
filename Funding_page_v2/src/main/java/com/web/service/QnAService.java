package com.web.service;

import com.web.domain.QnA;
import com.web.domain.User;
import com.web.repository.QnARepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QnAService {

    private final QnARepository qnaRepository;

    public QnAService(QnARepository qnaRepository) {
        this.qnaRepository = qnaRepository;
    }

    public List<QnA> findAll() {
        return qnaRepository.findAll();
    }

    public QnA saveQnA(QnA qna) {
        return qnaRepository.save(qna);
    }

    public QnA saveQnA(QnA qna, User user) {
        if (!qna.getAuthor().equals(user)) {
            throw new SecurityException("You are not the author of this QnA");
        }
        return qnaRepository.save(qna);
    }

    public QnA findById(Long id) {
        return qnaRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        qnaRepository.deleteById(id);
    }
}
