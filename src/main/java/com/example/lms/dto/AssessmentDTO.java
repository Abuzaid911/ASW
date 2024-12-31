package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssessmentDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private LocalDateTime dueDate;
    private Integer maxScore;
    private Long courseId;
    private List<QuestionDTO> questions;
}