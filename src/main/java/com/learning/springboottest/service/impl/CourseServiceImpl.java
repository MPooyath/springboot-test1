package com.learning.springboottest.service.impl;

import com.learning.springboottest.exception.UserNotFoundException;
import com.learning.springboottest.model.Course;
import com.learning.springboottest.repository.CourseRepository;
import com.learning.springboottest.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

private CourseRepository courseRepository;


    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;

    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

   public List<Course> findAllById(List<Long> courseIds){
        return courseRepository.findAllById(courseIds);
    }



    public Optional<Course> getCourseById(long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Course not found" + id));
        return Optional.of(course);
    }



    public Course updateCourse(long courseId, Course course) {

        Optional<Course> courseFromDB = courseRepository.findById(courseId);

        if (courseFromDB.isEmpty()) {
            throw new UserNotFoundException("Course with id:" + courseId + " does not exist");
        }

        courseFromDB.get().setCourseName(course.getCourseName());
        courseFromDB.get().setDescription(course.getDescription());
        if(course.getModules() != null) {
            courseFromDB.get().setModules(course.getModules());
        }

        return courseRepository.save(courseFromDB.get());
    }


}
