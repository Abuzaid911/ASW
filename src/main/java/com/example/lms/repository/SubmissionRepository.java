package com.example.lms.repository;

import com.example.lms.entity.Assessment;
import com.example.lms.entity.Submission;
import com.example.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssessment(Assessment assessment);
    List<Submission> findByStudent(User student);
    List<Submission> findByAssessmentAndStudent(Assessment assessment, User student);
    Optional<Submission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);
    boolean existsByAssessmentAndStudent(Assessment assessment, User student);
    List<Submission> findByAssessmentOrderBySubmissionDateDesc(Assessment assessment);
}