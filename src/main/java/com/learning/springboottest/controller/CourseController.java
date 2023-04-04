package com.learning.springboottest.controller;

import com.learning.springboottest.model.Course;
import com.learning.springboottest.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/course")
public class CourseController {


    private CourseService courseService;



    public CourseController( CourseService courseService) {

        this.courseService = courseService;

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@RequestBody Course course){
        return courseService.saveCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }



    @PutMapping("{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") long courseId,
                                           @RequestBody Course course){
        return new ResponseEntity<>(courseService.updateCourse(courseId,course), HttpStatus.OK);
    }

}





