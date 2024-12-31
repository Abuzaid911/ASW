package com.example.lms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentDTO extends AssessmentDTO {
    private List<String> allowedFileTypes;
    private Long maxFileSize; // in bytes
    private Boolean allowLateSubmission;
    private Integer latePenaltyPercent;
}