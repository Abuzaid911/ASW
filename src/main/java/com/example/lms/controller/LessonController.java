package com.example.lms.controller;

import com.example.lms.dto.LessonDTO;
import com.example.lms.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LessonDTO> createLesson(@PathVariable Long courseId,
                                                  @Valid @RequestBody LessonDTO lessonDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        LessonDTO createdLesson = lessonService.createLesson(lessonDTO, courseId, getUserId(userDetails));
        return ResponseEntity.ok(createdLesson);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
        LessonDTO lesson = lessonService.getLesson(id);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LessonDTO>> getCourseLessons(@PathVariable Long courseId) {
        List<LessonDTO> lessons = lessonService.getCourseLessons(courseId);
        return ResponseEntity.ok(lessons);
    }

    private Long getUserId(UserDetails userDetails) {
        // Implementation to get user ID from UserDetails
        return null; // Replace with actual implementation
    }
}