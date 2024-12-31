package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GradeDTO {
    private Long id;
    private Long studentId;
    private Long assessmentId;
    private Double score;
    private String feedback;
    private LocalDateTime gradedDate;
    private String gradedBy;
}