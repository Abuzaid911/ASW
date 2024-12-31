package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    // Store options as comma-separated string instead of collection
    @Column(name = "options")
    private String optionsString;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(nullable = false)
    private Integer points = 1;

    public enum QuestionType {
        MULTIPLE_CHOICE,
        TRUE_FALSE,
        SHORT_ANSWER
    }

    // Convert List to String for storage
    public void setOptions(List<String> options) {
        if (options != null) {
            this.optionsString = String.join(",", options);
        }
    }

    // Convert String back to List for use
    public List<String> getOptions() {
        if (optionsString != null && !optionsString.isEmpty()) {
            return Arrays.asList(optionsString.split(","));
        }
        return new ArrayList<>();
    }
}