package com.learning.springboottest.repository;

import com.learning.springboottest.model.Course;
import com.learning.springboottest.model.Enrollment;
import com.learning.springboottest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
}
