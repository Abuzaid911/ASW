package com.example.lms.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SubmissionDTO {
    private Long assessmentId;
    private Map<Long, String> answers;  // questionId -> answer
    private Double score;
    private String feedback;
}