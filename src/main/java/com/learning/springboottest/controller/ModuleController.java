package com.learning.springboottest.controller;

import com.learning.springboottest.service.CourseService;
import com.learning.springboottest.service.ModuleService;
import com.learning.springboottest.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuleController {
    private UserService userService;
    private CourseService courseService;

    private ModuleService moduleService;

    //private CourseProgressService courseProgressService;

    public ModuleController(UserService userService, CourseService courseService, ModuleService moduleService) {
        this.userService = userService;
        this.courseService = courseService;
        this.moduleService = moduleService;

    }
}
