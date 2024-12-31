package com.example.lms.controller;

import com.example.lms.dto.AttendanceDTO;
import com.example.lms.dto.LessonOTPDTO;
import com.example.lms.security.CustomUserDetails;
import com.example.lms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/lessons/{lessonId}/otp")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LessonOTPDTO> generateOTP(@PathVariable Long lessonId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LessonOTPDTO otp = attendanceService.generateOTP(lessonId, getUserId(userDetails));
        return ResponseEntity.ok(otp);
        
    }

    @PostMapping("/lessons/{lessonId}/mark")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AttendanceDTO> markAttendance(@PathVariable Long lessonId,
                                                        @RequestParam String otpCode,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        AttendanceDTO attendance = attendanceService.markAttendance(lessonId, otpCode, getUserId(userDetails));
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/lessons/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<AttendanceDTO>> getLessonAttendance(@PathVariable Long lessonId) {
        List<AttendanceDTO> attendances = attendanceService.getLessonAttendance(lessonId);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Map<String, List<AttendanceDTO>>> getCourseAttendance(@PathVariable Long courseId) {
        Map<String, List<AttendanceDTO>> attendances = attendanceService.getCourseAttendance(courseId);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<List<AttendanceDTO>> getStudentAttendance(@PathVariable Long studentId,
                                                                    @PathVariable Long courseId) {
        List<AttendanceDTO> attendances = attendanceService.getStudentAttendance(studentId, courseId);
        return ResponseEntity.ok(attendances);
    }

    private Long getUserId(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getUser().getId();
        }
        throw new IllegalStateException("UserDetails is not an instance of CustomUserDetails");
    }
}