package com.learning.springboottest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "module")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "module_name", nullable = false)
    private String moduleName;

    @Column(name = "points", nullable = false)
    private int points;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonBackReference
    private Course course;
    /*@JsonIgnore
    @ManyToMany(mappedBy = "completedModules")
    private List<User> completedUsers = new ArrayList<>();*/

    public Module() {

    }

    public Module(long id, String moduleName, int points) {
        this.id = id;
        this.moduleName = moduleName;
        this.points = points;
    }

    public Module(long id, String moduleName, int points, Course course) {
        this.id = id;
        this.moduleName = moduleName;
        this.points = points;
        this.course = course;

    }

    public Module(String moduleName, int points, Course course) {
        this.moduleName = moduleName;
        this.points = points;
        this.course = course;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", moduleName='" + moduleName + '\'' +
                ", points=" + points +
                ", course=" + course +
                '}';
    }

}
