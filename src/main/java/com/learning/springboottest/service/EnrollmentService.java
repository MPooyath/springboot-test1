package com.learning.springboottest.service;

public interface EnrollmentService {

    public void rewardPointsIfCourseCompletedByUser(Long userId);

    public void addCourseToUser(Long userId, Long courseId);

    public boolean isCourseCompletedByUser(Long userId, Long courseId);


    public void markModuleComplete(Long userId, Long courseId, Long moduleId);
}
