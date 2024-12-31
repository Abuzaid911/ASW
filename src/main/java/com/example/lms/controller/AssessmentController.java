package com.example.lms.controller;

import com.example.lms.dto.AssessmentDTO;
import com.example.lms.dto.QuizDTO;
import com.example.lms.dto.SubmissionDTO;
import com.example.lms.security.CustomUserDetails;
import com.example.lms.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping("/quiz")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<AssessmentDTO> createQuiz(@Valid @RequestBody QuizDTO quizDTO,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(assessmentService.createQuiz(quizDTO, getUserId(userDetails)));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AssessmentDTO>> getCourseAssessments(@PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.getCourseAssessments(courseId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AssessmentDTO> getAssessment(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessment(id));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<SubmissionDTO> submitAssessment(@PathVariable Long id,
                                                          @Valid @RequestBody SubmissionDTO submissionDTO,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(assessmentService.submitAssessment(id, submissionDTO, getUserId(userDetails)));
    }

    @GetMapping("/{id}/submissions")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<SubmissionDTO>> getAssessmentSubmissions(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentSubmissions(id));
    }

    @PutMapping("/submissions/{submissionId}/grade")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<SubmissionDTO> gradeSubmission(@PathVariable Long submissionId,
                                                         @Valid @RequestBody SubmissionDTO gradingDTO,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(assessmentService.gradeSubmission(submissionId, gradingDTO, getUserId(userDetails)));
    }

    private Long getUserId(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getUser().getId();
        }
        throw new IllegalStateException("UserDetails is not an instance of CustomUserDetails");
    }
}