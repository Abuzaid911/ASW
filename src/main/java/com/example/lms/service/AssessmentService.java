package com.example.lms.service;

import com.example.lms.dto.AssessmentDTO;
import com.example.lms.dto.QuizDTO;
import com.example.lms.dto.SubmissionDTO;
import com.example.lms.entity.*;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.exception.UnauthorizedException;
import com.example.lms.repository.AssessmentRepository;
import com.example.lms.repository.QuestionRepository;
import com.example.lms.repository.SubmissionRepository;
import com.example.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CourseRepository courseRepository;

    public AssessmentDTO createQuiz(QuizDTO quizDTO, Long instructorId) {
        Course course = courseRepository.findById(quizDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new UnauthorizedException("Only course instructor can create quizzes");
        }

        Assessment assessment = new Assessment();
        assessment.setTitle(quizDTO.getTitle());
        assessment.setDescription(quizDTO.getDescription());
        assessment.setType(Assessment.AssessmentType.QUIZ);
        assessment.setCourse(course);
        assessment.setDueDate(quizDTO.getDueDate());
        assessment.setMaxScore(quizDTO.getMaxScore());

        Assessment savedAssessment = assessmentRepository.save(assessment);

        // Create questions
        quizDTO.getQuestions().forEach(questionDTO -> {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setType(Question.QuestionType.valueOf(questionDTO.getType().name()));
            question.setOptions(questionDTO.getOptions());
            question.setCorrectAnswer(questionDTO.getCorrectAnswer());
            question.setPoints(questionDTO.getPoints());
            question.setAssessment(savedAssessment);
            questionRepository.save(question);
        });

        return convertToDTO(savedAssessment);
    }

    public List<SubmissionDTO> getAssessmentSubmissions(Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        List<Submission> submissions = submissionRepository.findByAssessmentOrderBySubmissionDateDesc(assessment);
        return submissions.stream()
                .map(this::convertToSubmissionDTO)
                .collect(Collectors.toList());
    }

    public SubmissionDTO gradeSubmission(Long submissionId, SubmissionDTO gradingDTO, Long instructorId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        // Verify instructor is teaching this course
        if (!submission.getAssessment().getCourse().getInstructor().getId().equals(instructorId)) {
            throw new UnauthorizedException("Only course instructor can grade submissions");
        }

        submission.setScore(gradingDTO.getScore());
        submission.setFeedback(gradingDTO.getFeedback());
        submission.setGradedBy(instructorId);
        submission.setGradedAt(LocalDateTime.now());

        Submission gradedSubmission = submissionRepository.save(submission);
        return convertToSubmissionDTO(gradedSubmission);
    }

    public List<AssessmentDTO> getCourseAssessments(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return assessmentRepository.findByCourse(course).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssessmentDTO getAssessment(Long id) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));
        return convertToDTO(assessment);
    }

    public SubmissionDTO submitAssessment(Long assessmentId, SubmissionDTO submissionDTO, Long studentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        if (assessment.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Assessment submission deadline has passed");
        }

        Submission submission = new Submission();
        submission.setAssessment(assessment);
        submission.setStudent(new User()); // Need to set proper student
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setAnswers(submissionDTO.getAnswers());

        if (assessment.getType() == Assessment.AssessmentType.QUIZ) {
            // Auto-grade quiz
            double score = calculateQuizScore(assessment, submissionDTO.getAnswers());
            submission.setScore(score);
        }

        Submission savedSubmission = submissionRepository.save(submission);
        return convertToSubmissionDTO(savedSubmission);
    }

    private double calculateQuizScore(Assessment assessment, Map<Long, String> answers) {
        List<Question> questions = questionRepository.findByAssessment(assessment);
        double totalPoints = 0;
        double earnedPoints = 0;

        for (Question question : questions) {
            String studentAnswer = answers.get(question.getId());
            if (studentAnswer != null && studentAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                earnedPoints += question.getPoints();
            }
            totalPoints += question.getPoints();
        }

        return (earnedPoints / totalPoints) * 100;
    }

    private AssessmentDTO convertToDTO(Assessment assessment) {
        AssessmentDTO dto = new AssessmentDTO();
        dto.setId(assessment.getId());
        dto.setTitle(assessment.getTitle());
        dto.setDescription(assessment.getDescription());
        dto.setType(assessment.getType().name());
        dto.setDueDate(assessment.getDueDate());
        dto.setMaxScore(assessment.getMaxScore());
        dto.setCourseId(assessment.getCourse().getId());
        return dto;
    }

    private SubmissionDTO convertToSubmissionDTO(Submission submission) {
        SubmissionDTO dto = new SubmissionDTO();
        dto.setAssessmentId(submission.getAssessment().getId());
        dto.setAnswers(submission.getAnswers());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());
        return dto;
    }
}