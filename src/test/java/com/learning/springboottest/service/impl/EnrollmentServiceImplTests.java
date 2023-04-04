package com.learning.springboottest.service.impl;

import com.learning.springboottest.model.Course;
import com.learning.springboottest.model.Enrollment;
import com.learning.springboottest.model.Module;
import com.learning.springboottest.model.User;
import com.learning.springboottest.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class EnrollmentServiceImplTests {

    private static final Logger logger = Logger.getLogger(EnrollmentServiceImplTests.class.getName());

    @Mock
    private UserServiceImpl userService;

    @Mock
    private CourseServiceImpl courseService;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testAddCourseToUser() {
        // Arrange
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Course course = new Course(1L, "SpringBoot", "SB");
        Enrollment enrollment = new Enrollment(user, course);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        // Act
        enrollmentService.addCourseToUser(1L, 1L);

        // Assert
        assertEquals(1, user.getEnrollments().size());
        assertEquals(user.getEnrollments().iterator().next().getCourse(), course);
    }

    @Test
    public void testIsCourseCompletedByUser() {
        // Arrange
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Course course = new Course(1L, "SpringBoot", "SB");
        Module module = new Module(1l, "Intro", 10);
        course.addModule(module);
        Enrollment enrollment = new Enrollment(user, course);
        enrollment.completeModule(module);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByUserAndCourse(user, course)).thenReturn(Optional.of(enrollment));
        // Act
        boolean result = enrollmentService.isCourseCompletedByUser(1L, 1L);

        // Assert
        assertTrue(enrollment.isCompleted());
        assertEquals(module.getId(), enrollment.getCompletedModuleIds().iterator().next());
        assertEquals(1, enrollment.getCompletedModuleIds().size());
        assertTrue(result);
    }

    @Test
    public void markModuleCompleteTest() {

        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Course course = new Course(1L, "SpringBoot", "SB");
        Module module1 = new Module(1l, "Intro", 10);
        Module module2= new Module(1l, "Intro", 10);
        Module module3 = new Module(1l, "Intro", 10);
        course.addModule(module1);
        course.addModule(module2);
        course.addModule(module3);
        Enrollment enrollment = new Enrollment(user, course);
        Set<Enrollment> enrollments = new HashSet<>();
        enrollments.add(enrollment);
        user.setEnrollments(enrollments);

        when(userService.getUserById(anyLong())).thenReturn(Optional.of(user));
        when(courseService.getCourseById(anyLong())).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByUserAndCourse(any(User.class), any(Course.class))).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        doAnswer(invocation -> {
            Object arg = invocation.getArgument(1);
            if (arg instanceof User) {
                User savedUser = (User) arg;
                assertThat(savedUser.getRewardPoints()).isEqualTo(30) ;
            }
            return null;
        }).when(userService).updatePoints(1L, user);

        enrollmentService.markModuleComplete(1L, 1L, 1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);

        verify(enrollmentRepository).save(enrollmentCaptor.capture());
        verify(userService).updatePoints(userIdCaptor.capture(), userCaptor.capture());
        verify(userService).updatePoints(eq(1L), userCaptor.capture());
        verify(userService).updatePoints(eq(1L), eq(user));


        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
        verify(userService, times(1)).updatePoints(user.getId(), user);
        verify(courseService, times(1)).getCourseById(course.getId());
        verify(userService, times(1)).getUserById(user.getId());

        Enrollment savedEnrollment = enrollmentCaptor.getValue();
        User updatedUser = userCaptor.getValue();
        Long updatedUserId = userIdCaptor.getValue();

        assertThat(savedEnrollment.isCompleted()).isTrue();
        assertThat(savedEnrollment.getCompletedModuleIds()).contains(module1.getId());
        assertThat(updatedUser.getRewardPoints()).isEqualTo(30);
        assertThat(updatedUserId).isEqualTo(1L);

    }
    @Test
   public void testTotalPointsForAUser() {
        // Arrange
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Course course = new Course(1L, "SpringBoot", "SB");
        Module module = new Module(1l, "Intro", 20);
        Enrollment enrollment1 = new Enrollment(user, course);
        course.addModule(module);
        enrollment1.completeModule(module);

        Course course1 = new Course(2L, "Docker", "DC");
        Module module1 = new Module(2l, "Intro", 10);
        Enrollment enrollment2 = new Enrollment(user,course1);
        course1.addModule(module1);
        enrollment2.completeModule(module1);


        Set<Enrollment> enrollments = new HashSet<>();
        enrollments.add(enrollment1);
        enrollments.add(enrollment2);
        // Act
        int totalPoints = enrollmentService.totalPointsForAUser(enrollments);
        // Assert
        assertEquals(30, totalPoints);
    }
    @Test
    public void testRewardPointsIfCourseCompletedByUser() {
        // Arrange
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Course course = new Course(1L, "SpringBoot", "SB");
        Module module = new Module(1l, "Intro", 10);
        Enrollment enrollment = new Enrollment(user, course);
        course.addModule(module);
        enrollment.completeModule(module);
        user.getEnrollments().add(enrollment);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(userService.updateUser(1l,user)).thenReturn(user);
        // Act
        enrollmentService.rewardPointsIfCourseCompletedByUser(1L);

        // Assert
        assertTrue(enrollment.isCompleted());
        assertNotNull(user.getRewardPoints());
       assertEquals((course.getModules().size())* 10, user.getRewardPoints());
    }

    @Test
    public void testRewardPointsIfUserNotEnrolledInAnyCourse() {
        // Arrange
        long userId = 1L;
        User user = new User(userId, "John", "Doe", "john.doe@example.com");
        user.setEnrollments(new HashSet<>());

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
       //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.rewardPointsIfCourseCompletedByUser(userId);
        });
    }

    @Test
    public void testRewardPointsIfInvalidUserId() {
        long userId = 1l;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.rewardPointsIfCourseCompletedByUser(userId);
        });
    }
}









