package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonDTO {
    private Long id;

    @NotBlank(message = "Lesson title cannot be blank")
    private String title;

    @NotBlank(message = "Lesson content cannot be blank")
    private String content;

    private String otpCode;
    private LocalDateTime otpExpiry;
    private Long courseId;
    private String courseName;
}