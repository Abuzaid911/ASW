package com.example.lms.repository;

import com.example.lms.entity.Assessment;
import com.example.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByCourse(Course course);
    List<Assessment> findByCourseId(Long courseId);
    List<Assessment> findByCourseAndType(Course course, Assessment.AssessmentType type);
    boolean existsByCourseAndTitle(Course course, String title);
}