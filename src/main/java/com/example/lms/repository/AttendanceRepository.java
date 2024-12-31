package com.example.lms.repository;

import com.example.lms.entity.Attendance;
import com.example.lms.entity.Lesson;
import com.example.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByLessonAndStudent(Lesson lesson, User student);

    List<Attendance> findByLesson(Lesson lesson);
    List<Attendance> findByLessonCourseIdAndStudent(Long courseId, User student);

    List<Attendance> findByStudent(User student);

    List<Attendance> findByLessonAndStudent(Lesson lesson, User student);

    List<Attendance> findByLessonCourseId(Long courseId);

    long countByLessonAndStudent(Lesson lesson, User student);
}