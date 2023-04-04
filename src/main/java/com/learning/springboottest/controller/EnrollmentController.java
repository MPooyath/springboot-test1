package com.learning.springboottest.controller;

import com.learning.springboottest.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("enrolled")
public class EnrollmentController {


    private EnrollmentService enrollmentService;



    public EnrollmentController( EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{userId}/courses/{courseId}/modules/{moduleId}/complete")
    public void completeModule(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long moduleId) {
        enrollmentService.markModuleComplete(userId, courseId, moduleId);
    }
    @PutMapping("/reward-points/{userId}")
    public void rewardPointsIfCourseCompletedByUser(@PathVariable Long userId) {
        enrollmentService.rewardPointsIfCourseCompletedByUser(userId);
    }
    @GetMapping("/is-completed/{userId}/{courseId}")
    public boolean isCourseCompletedByUser(@PathVariable Long userId, @PathVariable Long courseId) {
        return enrollmentService.isCourseCompletedByUser(userId, courseId);
    }

    @PostMapping("/{userId}/enroll/{courseId}")
    public void enrollCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        enrollmentService.addCourseToUser(userId, courseId);
    }

}
