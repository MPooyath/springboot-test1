package com.learning.springboottest.service;

import com.learning.springboottest.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course saveCourse(Course course);

    List<Course> getAllCourses();

    public Course updateCourse(long courseId, Course course);

   // public Set<Course> enrollCourses(User user, Set<Course> courses);


//    public void enrollCourses(User user, List<Course> courses);

    List<Course> findAllById(List<Long> courseIds);
    public Optional<Course> getCourseById(long id);

}
