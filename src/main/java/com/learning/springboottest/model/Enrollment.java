package com.learning.springboottest.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ElementCollection
    @CollectionTable(name = "enrollment_completed_modules", joinColumns = {@JoinColumn(name = "enrollment_id")})
    @Column(name = "module_id")
    private Set<Long> completedModuleIds = new HashSet<>();

    public Enrollment() {}

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Long> getCompletedModuleIds() {
        return completedModuleIds;
    }

    public void setCompletedModuleIds(Set<Long> completedModuleIds) {
        this.completedModuleIds = completedModuleIds;
    }

    public void completeModule(Module module) {
        completedModuleIds.add(module.getId());
    }

    public boolean isCompleted() {
        Set<Long> moduleIds = course.getModules().stream()
                .map(Module::getId)
                .collect(Collectors.toSet());
        return moduleIds.equals(completedModuleIds);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", course=" + course.getId() +
                ", completedModuleIds=" + completedModuleIds +
                '}';
    }
}


