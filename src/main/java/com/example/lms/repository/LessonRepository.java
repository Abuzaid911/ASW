package com.example.lms.repository;

import com.example.lms.entity.Course;
import com.example.lms.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseOrderByIdAsc(Course course);
}