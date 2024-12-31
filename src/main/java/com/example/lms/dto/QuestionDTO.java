package com.example.lms.dto;

import com.example.lms.entity.Question;
import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String questionText;
    private Question.QuestionType type;
    private List<String> options;
    private String correctAnswer;
    private Integer points;
    private Long assessmentId;
}