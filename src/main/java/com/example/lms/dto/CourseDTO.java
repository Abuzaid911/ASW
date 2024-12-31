package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;

@Data
public class CourseDTO {
    private Long id;

    @NotBlank(message = "Course title cannot be blank")
    private String title;

    @NotBlank(message = "Course description cannot be blank")
    private String description;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    private Long instructorId;
    private String instructorName;
    private List<LessonDTO> lessons;
    private int enrolledStudentsCount;
}