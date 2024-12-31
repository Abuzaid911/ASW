package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizDTO {
    private String title;
    private String description;
    private Long courseId;
    private LocalDateTime dueDate;
    private Integer maxScore;
    private List<QuestionDTO> questions;  // Added this field
}