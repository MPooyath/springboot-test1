package com.learning.springboottest.service.impl;

import com.learning.springboottest.model.Course;
import com.learning.springboottest.model.Enrollment;
import com.learning.springboottest.model.Module;
import com.learning.springboottest.model.User;
import com.learning.springboottest.repository.EnrollmentRepository;
import com.learning.springboottest.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger logger = Logger.getLogger(EnrollmentServiceImpl.class.getName());

    private EnrollmentRepository enrollmentRepository;

    private UserServiceImpl userService;

    private CourseServiceImpl courseService;


    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserServiceImpl userService, CourseServiceImpl courseService) {

        this.enrollmentRepository = enrollmentRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    public void addCourseToUser(Long userId, Long courseId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseService.getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Enrollment enrollment = new Enrollment(user, course);
        user.getEnrollments().add(enrollment);
        enrollmentRepository.save(enrollment);
    }

    public boolean isCourseCompletedByUser(Long userId, Long courseId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        Course course = courseService.getCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Invalid course id"));
        Optional<Enrollment> enrollment = enrollmentRepository.findByUserAndCourse(user, course);
        if (enrollment.isEmpty()) {
            throw new IllegalArgumentException("User is not enrolled in this course");
        }
        return enrollment.get().isCompleted();
    }

    public void markModuleComplete(Long userId, Long courseId, Long moduleId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseService.getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Module module = course.getModules().stream()
                .filter(m -> m.getId()==(moduleId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Enrollment enrollment = enrollmentRepository.findByUserAndCourse(user, course)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.completeModule(module);
        Set<Enrollment> enrollments = user.getEnrollments();
        enrollments.add(enrollment);
        if (enrollments.isEmpty()) {
            throw new IllegalArgumentException("User is not enrolled in any course");
        }
        int points = totalPointsForAUser(enrollments);
        user.setRewardPoints(points);
        enrollmentRepository.save(enrollment);
        userService.updatePoints(userId,user);

    }


    int totalPointsForAUser(Set<Enrollment> enrollments) {
        int totalPoints = 0;
        for (Enrollment enrollment : enrollments) {

            if (enrollment.isCompleted()) {
                for (Module module : enrollment.getCourse().getModules()){

                    totalPoints += module.getPoints();
                }
            } else {
                return totalPoints;
            }
        }
        return totalPoints;
    }
    public void rewardPointsIfCourseCompletedByUser(Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        Set<Enrollment> enrollments = user.getEnrollments();
        if (enrollments.isEmpty()) {
            throw new IllegalArgumentException("User is not enrolled in any course");
        }
            int points = totalPointsForAUser(enrollments);
            user.setRewardPoints(points);
            userService.updateUser(userId,user);

    }



}
