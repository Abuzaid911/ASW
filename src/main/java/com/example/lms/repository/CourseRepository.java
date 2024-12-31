package com.example.lms.repository;

import com.example.lms.entity.Course;
import com.example.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
    List<Course> findByEnrollmentsStudent(User student);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Course c WHERE c.instructor = ?1 AND c.id = ?2")
    boolean isInstructorOfCourse(User instructor, Long courseId);
}