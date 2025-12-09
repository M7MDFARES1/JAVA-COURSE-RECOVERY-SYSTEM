package com.mycompany.crs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Student entity class - Represents a student in the CRS system
 * 
 * OOP Concepts Demonstrated:
 * - Encapsulation: Private fields with public getters/setters
 * - Serializable: For binary file storage (as per requirements)
 * - Modularity: Single responsibility - manages student data only
 * 
 * @author YourName
 * @version 1.0
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Private fields - Encapsulation
    private String studentId;
    private String firstName;
    private String lastName;
    private String major;
    private String year; // Freshman, Sophomore, Junior, Senior
    private String email;
    private List<CourseEnrollment> enrolledCourses;
    private boolean isEligible;
    private String enrollmentStatus; // "Not Enrolled", "Enrolled", "Pending"
    
    /**
     * Full constructor
     */
    public Student(String studentId, String firstName, String lastName, 
                   String major, String year, String email) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.year = year;
        this.email = email;
        this.enrolledCourses = new ArrayList<>();
        this.enrollmentStatus = "Not Enrolled";
        this.isEligible = false;
    }
    
    /**
     * Default constructor
     */
    public Student() {
        this.enrolledCourses = new ArrayList<>();
        this.enrollmentStatus = "Not Enrolled";
        this.isEligible = false;
    }
    
    // Getters and Setters - Encapsulation principle
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<CourseEnrollment> getEnrolledCourses() {
        return enrolledCourses;
    }
    
    public void setEnrolledCourses(List<CourseEnrollment> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
    
    public void addCourseEnrollment(CourseEnrollment enrollment) {
        this.enrolledCourses.add(enrollment);
    }
    
    public boolean isEligible() {
        return isEligible;
    }
    
    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }
    
    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }
    
    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }
    
    /**
     * Calculate CGPA based on all enrolled courses
     * Formula: CGPA = (Total Grade Points) / (Total Credit Hours)
     * 
     * This is the CORE calculation required by the assignment!
     * 
     * @return calculated CGPA (0.0 if no courses)
     */
    public double calculateCGPA() {
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            return 0.0;
        }
        
        double totalGradePoints = 0.0;
        int totalCreditHours = 0;
        
        for (CourseEnrollment enrollment : enrolledCourses) {
            double gradePoint = enrollment.getGradePoint();
            int credits = enrollment.getCourse().getCredits();
            
            totalGradePoints += gradePoint * credits;
            totalCreditHours += credits;
        }
        
        if (totalCreditHours == 0) {
            return 0.0;
        }
        
        // Round to 2 decimal places
        return Math.round((totalGradePoints / totalCreditHours) * 100.0) / 100.0;
    }
    
    /**
     * Count number of failed courses
     * A course is failed if grade point < 2.0 (below C grade)
     * 
     * @return number of failed courses
     */
    public int countFailedCourses() {
        if (enrolledCourses == null) {
            return 0;
        }
        
        int failedCount = 0;
        for (CourseEnrollment enrollment : enrolledCourses) {
            if (enrollment.getGradePoint() < 2.0) {
                failedCount++;
            }
        }
        return failedCount;
    }
    
    /**
     * Get list of failed courses
     * 
     * @return List of failed CourseEnrollment objects
     */
    public List<CourseEnrollment> getFailedCourses() {
        List<CourseEnrollment> failedCourses = new ArrayList<>();
        if (enrolledCourses != null) {
            for (CourseEnrollment enrollment : enrolledCourses) {
                if (enrollment.getGradePoint() < 2.0) {
                    failedCourses.add(enrollment);
                }
            }
        }
        return failedCourses;
    }
    
    /**
     * Check eligibility for progression to next level
     * 
     * Eligibility Criteria (as per assignment requirements):
     * 1. CGPA must be >= 2.0
     * 2. Failed courses must be <= 3
     * 
     * @return true if eligible, false otherwise
     */
    public boolean checkEligibility() {
        double cgpa = calculateCGPA();
        int failedCourses = countFailedCourses();
        
        this.isEligible = (cgpa >= 2.0 && failedCourses <= 3);
        return this.isEligible;
    }
    
    /**
     * Get detailed eligibility reason/status message
     * 
     * @return String describing why student is/isn't eligible
     */
    public String getEligibilityReason() {
        double cgpa = calculateCGPA();
        int failedCourses = countFailedCourses();
        
        if (cgpa < 2.0 && failedCourses > 3) {
            return String.format("CGPA below 2.0 (%.2f) AND more than 3 failed courses (%d)", 
                               cgpa, failedCourses);
        } else if (cgpa < 2.0) {
            return String.format("CGPA below 2.0 (Current: %.2f)", cgpa);
        } else if (failedCourses > 3) {
            return String.format("More than 3 failed courses (Failed: %d)", failedCourses);
        } else {
            return "Eligible for progression";
        }
    }
    
    /**
     * Get year level as integer (for progression logic)
     */
    public int getYearLevel() {
        switch (year) {
            case "Freshman": return 1;
            case "Sophomore": return 2;
            case "Junior": return 3;
            case "Senior": return 4;
            default: return 1;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Student{ID='%s', Name='%s %s', Major='%s', Year='%s', CGPA=%.2f, Failed=%d}",
                studentId, firstName, lastName, major, year, calculateCGPA(), countFailedCourses());
    }
}