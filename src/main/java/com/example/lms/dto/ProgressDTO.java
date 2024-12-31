package com.example.lms.dto;

import lombok.Data;

@Data
public class ProgressDTO {
    private Long courseId;
    private Long studentId;
    private Double completionPercentage;
    private Integer completedLessons;
    private Integer totalLessons;
    private Double averageGrade;
    private Double attendanceRate;
}