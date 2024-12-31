package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttendanceDTO {
    private Long id;
    private Long lessonId;
    private String lessonTitle;
    private Long studentId;
    private String studentName;
    private LocalDateTime attendanceTime;
    private String otpCode;
}