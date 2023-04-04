package com.learning.springboottest.service.impl;

import com.learning.springboottest.exception.UserNotFoundException;
import com.learning.springboottest.model.Course;
import com.learning.springboottest.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CourseServiceImplTests {
    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCourse_ShouldReturnSavedCourse() {
        //Arrange
        Course course = new Course(1l,"SpringBoot","SB");
        when(courseRepository.save(course)).thenReturn(course);
        //Act
        Course savedCourse = courseService.saveCourse(course);
        //Assert
        assertThat(savedCourse).isNotNull();
        assertEquals(course.getCourseName(), savedCourse.getCourseName());
        assertEquals(course.getDescription(), savedCourse.getDescription());
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourses() {
        //Arrange
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course(1l,"SpringBoot","SB");
        Course course2= new Course(2l,"Docker","DC");
        courses.add(course1);
        courses.add(course2);
        when(courseRepository.findAll()).thenReturn(courses);
         //Act
        List<Course> foundCourses = courseService.getAllCourses();
        //Assert
        assertThat(foundCourses).isNotNull().hasSize(courses.size());
        assertThat(foundCourses).containsExactlyElementsOf(courses);
    }

    @Test
    void getCourseById_ShouldReturnCourseIfExists() {
        //Arrange
        long courseId = 1L;
        Course course = new Course(courseId,"SpringBoot","SB");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
         //Act
        Optional<Course> foundCourse = courseService.getCourseById(courseId);
        //Assert
        assertThat(foundCourse).isPresent().contains(course);
    }

    @Test
    void getCourseById_ShouldThrowExceptionIfCourseDoesNotExist() {
        //Arrange
        long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(UserNotFoundException.class, () -> courseService.getCourseById(courseId));
    }

    @Test
    public void updateCourse_shouldUpdateCourseWhenIdExists() {

        // Arrange
        long courseId = 1L;
        Course existingCourse = new Course(courseId, "SpringBoot", "SB");
        Course updatedCourse = new Course(courseId, "Docker", "DC");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        // Act
        Course result = courseService.updateCourse(courseId, updatedCourse);

        // Assert
        assertEquals(updatedCourse.getCourseName(), result.getCourseName());
        assertEquals(updatedCourse.getDescription(), result.getDescription());
        assertEquals(courseId, result.getId());
    }

    @Test
    public void updateCourse_shouldThrowUserNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        long nonExistentId = 12345L;
        Course updatedCourse = new Course("SpringBoot", "SB");

        // Act and assert
        assertThrows(UserNotFoundException.class, () -> courseService.updateCourse(nonExistentId, updatedCourse));
    }

}