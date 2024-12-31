package com.example.lms.service;

import com.example.lms.dto.LessonDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Lesson;
import com.example.lms.entity.User;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.exception.UnauthorizedException;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    public LessonDTO createLesson(LessonDTO lessonDTO, Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!courseRepository.isInstructorOfCourse(course.getInstructor(), courseId)) {
            throw new UnauthorizedException("Only the course instructor can add lessons");
        }

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);
        return convertToDTO(savedLesson);
    }

    public LessonDTO getLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return convertToDTO(lesson);
    }

    public List<LessonDTO> getCourseLessons(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return lessonRepository.findByCourseOrderByIdAsc(course).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setCourseId(lesson.getCourse().getId());
        dto.setCourseName(lesson.getCourse().getTitle());
        return dto;
    }
}