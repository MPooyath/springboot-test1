package com.learning.springboottest.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "course_name", nullable = false)
    private String courseName;
    @Column(name = "description", nullable = false)
    private String description;


   /* @ElementCollection
    @MapKeyColumn(name = "module")
    @Column(name = "flag")
    @CollectionTable(name = "course_module")
    private Map<String,Boolean> courseModules;*/


  /* @JsonIgnore
    @ManyToMany(mappedBy = "enrolledCourses")
    private List<User> users = new ArrayList<>();*/

 /*   @OneToMany(targetEntity = Module.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="course_id",referencedColumnName = "id")
    private List<Module> modules;*/

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Module> modules = new HashSet<>();


 /*   @JsonIgnore
    @ManyToMany(mappedBy = "enrolledCourses")
    private List<User> enrolledUsers = new ArrayList<>();*/




    public Course() {

    }

    public Course(String courseName, String description) {
        this.courseName = courseName;
        this.description = description;

    }

    public Course(String courseName, String description, Set<Module> modules) {
        this.courseName = courseName;
        this.description = description;
        this.modules = modules;

    }


    public Course(long id, String courseName, String description) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

/*public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }*/

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }


    public void addModule(Module module) {
        module.setCourse(this);
        modules.add(module);
    }

    public void removeModule(Module module) {
        modules.remove(module);
        module.setCourse(null);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", modules=" + modules +
                '}';
    }
/*public boolean isCompleted(User user) {
        for (Module module : modules) {
            if (!module.isCompleted(user)) {
                return false;
            }
        }
        return true;
    }

    public void complete(User user) {
        user.rewardPoints();
    }*/
}
