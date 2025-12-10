package com.mycompany.crs.model;

import java.io.Serializable;

/**
 * Course entity class - Represents a course in the system
 * 
 * OOP Concepts Demonstrated:
 * - Encapsulation: Private fields with controlled access
 * - Serializable: For binary file persistence
 * 
 * @author YourName
 * @version 1.0
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Private fields - Encapsulation
    private String courseId;
    private String courseName;
    private int credits;
    private String semester;
    private String instructor;
    private int examWeight;
    private int assignmentWeight;
    
    /**
     * Full constructor
     */
    public Course(String courseId, String courseName, int credits, 
                  String semester, String instructor, int examWeight, int assignmentWeight) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.instructor = instructor;
        this.examWeight = examWeight;
        this.assignmentWeight = assignmentWeight;
    }
    
    /**
     * Default constructor
     */
    public Course() {
    }
    
    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public int getExamWeight() {
        return examWeight;
    }
    
    public void setExamWeight(int examWeight) {
        this.examWeight = examWeight;
    }
    
    public int getAssignmentWeight() {
        return assignmentWeight;
    }
    
    public void setAssignmentWeight(int assignmentWeight) {
        this.assignmentWeight = assignmentWeight;
    }
    
    @Override
    public String toString() {
        return courseId + " - " + courseName + " (" + credits + " credits)";
    }
}