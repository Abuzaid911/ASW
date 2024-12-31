package com.example.lms.service;

import com.example.lms.dto.AttendanceDTO;
import com.example.lms.dto.LessonOTPDTO;
import com.example.lms.entity.Attendance;
import com.example.lms.entity.Course;
import com.example.lms.entity.Lesson;
import com.example.lms.entity.User;
import com.example.lms.exception.DuplicateAttendanceException;
import com.example.lms.exception.InvalidOTPException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.exception.UnauthorizedException;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LessonRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public LessonOTPDTO generateOTP(Long lessonId, Long instructorId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Verify instructor
        if (!lesson.getCourse().getInstructor().getId().equals(instructorId)) {
            throw new UnauthorizedException("Only the course instructor can generate OTP");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

        lesson.setOtpCode(otp);
        lesson.setOtpExpiry(expiryTime);
        lessonRepository.save(lesson);

        LessonOTPDTO otpDTO = new LessonOTPDTO();
        otpDTO.setOtpCode(otp);
        otpDTO.setExpiryTime(expiryTime);
        return otpDTO;
    }

    public AttendanceDTO markAttendance(Long lessonId, String otpCode, Long studentId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Validate OTP
        if (!lesson.getOtpCode().equals(otpCode) ||
                LocalDateTime.now().isAfter(lesson.getOtpExpiry())) {
            throw new InvalidOTPException("Invalid or expired OTP");
        }

        // Check for duplicate attendance
        if (attendanceRepository.existsByLessonAndStudent(lesson, student)) {
            throw new DuplicateAttendanceException("Attendance already marked for this lesson");
        }

        Attendance attendance = new Attendance();
        attendance.setLesson(lesson);
        attendance.setStudent(student);
        attendance.setAttendanceTime(LocalDateTime.now());
        attendance.setOtpUsed(otpCode);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public List<AttendanceDTO> getLessonAttendance(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        return attendanceRepository.findByLesson(lesson).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, List<AttendanceDTO>> getCourseAttendance(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        List<Attendance> attendances = attendanceRepository.findByLessonCourseId(courseId);

        return attendances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.groupingBy(dto -> dto.getLessonTitle()));
    }

    public List<AttendanceDTO> getStudentAttendance(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return attendanceRepository.findByLessonCourseIdAndStudent(courseId, student).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setLessonId(attendance.getLesson().getId());
        dto.setLessonTitle(attendance.getLesson().getTitle());
        dto.setStudentId(attendance.getStudent().getId());
        dto.setStudentName(attendance.getStudent().getUsername());
        dto.setAttendanceTime(attendance.getAttendanceTime());
        dto.setOtpCode(attendance.getOtpUsed());
        return dto;
    }
}