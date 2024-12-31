package com.example.lms.repository;

import com.example.lms.entity.Assessment;
import com.example.lms.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssessment(Assessment assessment);
    List<Question> findByAssessmentId(Long assessmentId);
    void deleteByAssessmentId(Long assessmentId);
    long countByAssessment(Assessment assessment);
}