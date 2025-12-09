package com.mycompany.crs.model;

import java.io.Serializable;

/**
 * CourseEnrollment - Links a student to a course with grades
 * 
 * OOP Concepts Demonstrated:
 * - Composition: Has-a relationship (Student HAS enrollments)
 * - Encapsulation: Grade calculation logic encapsulated
 * 
 * @author YourName
 * @version 1.0
 */
public class CourseEnrollment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Course course;
    private int examScore;
    private int assignmentScore;
    private double totalScore;
    private String letterGrade;
    private double gradePoint;
    
    /**
     * Constructor
     */
    public CourseEnrollment(Course course, int examScore, int assignmentScore) {
        this.course = course;
        this.examScore = examScore;
        this.assignmentScore = assignmentScore;
        calculateGrades();
    }
    
    /**
     * Default constructor
     */
    public CourseEnrollment() {
    }
    
    /**
     * Calculate total score, letter grade, and grade point
     * This automatically converts scores to grades
     */
    public void calculateGrades() {
        // Calculate weighted total score
        this.totalScore = examScore + assignmentScore;
        
        // Convert to letter grade
        this.letterGrade = scoreToLetterGrade(totalScore);
        
        // Convert to grade point (4.0 scale)
        this.gradePoint = letterToGradePoint(letterGrade);
    }
    
    /**
     * Convert total score to letter grade
     */
    private String scoreToLetterGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 85) return "A-";
        else if (score >= 80) return "B+";
        else if (score >= 75) return "B";
        else if (score >= 70) return "B-";
        else if (score >= 65) return "C+";
        else if (score >= 60) return "C";
        else if (score >= 55) return "C-";
        else if (score >= 50) return "D";
        else return "F";
    }
    
    /**
     * Convert letter grade to grade point (4.0 scale)
     */
    private double letterToGradePoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    /**
     * Check if this course enrollment is a failure
     */
    public boolean isFailed() {
        return gradePoint < 2.0;
    }
    
    // Getters and Setters
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public int getExamScore() {
        return examScore;
    }
    
    public void setExamScore(int examScore) {
        this.examScore = examScore;
        calculateGrades();
    }
    
    public int getAssignmentScore() {
        return assignmentScore;
    }
    
    public void setAssignmentScore(int assignmentScore) {
        this.assignmentScore = assignmentScore;
        calculateGrades();
    }
    
    public double getTotalScore() {
        return totalScore;
    }
    
    public String getLetterGrade() {
        return letterGrade;
    }
    
    public double getGradePoint() {
        return gradePoint;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s (%.1f) - GP: %.1f", 
                course.getCourseId(), letterGrade, totalScore, gradePoint);
    }
}