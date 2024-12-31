package com.example.lms.controller;

import com.example.lms.dto.CourseDTO;
import com.example.lms.security.CustomUserDetails;
import com.example.lms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO, getUserId(userDetails));
        return ResponseEntity.ok(createdCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        CourseDTO course = courseService.getCourse(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CourseDTO>> getInstructorCourses(@AuthenticationPrincipal UserDetails userDetails) {
        List<CourseDTO> courses = courseService.getInstructorCourses(getUserId(userDetails));
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(@AuthenticationPrincipal UserDetails userDetails) {
        List<CourseDTO> courses = courseService.getStudentCourses(getUserId(userDetails));
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> enrollInCourse(@PathVariable Long courseId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.enrollStudent(courseId, getUserId(userDetails));
        return ResponseEntity.ok().build();
    }

    private Long getUserId(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getUser().getId();
        }
        throw new IllegalStateException("UserDetails is not an instance of CustomUserDetails");
    }
}