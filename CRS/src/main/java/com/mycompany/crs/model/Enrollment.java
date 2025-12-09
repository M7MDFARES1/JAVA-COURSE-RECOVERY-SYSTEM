package com.mycompany.crs.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enrollment - Records when a student enrolls for next semester
 * 
 * OOP Concepts Demonstrated:
 * - Encapsulation: Controlled access to enrollment data
 * - Composition: References Student through studentId
 * 
 * @author YourName
 * @version 1.0
 */
public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String enrollmentId;
    private String studentId;
    private String nextSemester;
    private String nextYear;
    private LocalDateTime enrollmentDate;
    private String enrolledBy;
    private String status;
    private String remarks;
    
    /**
     * Constructor
     */
    public Enrollment(String enrollmentId, String studentId, String nextSemester, 
                     String nextYear, String enrolledBy) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.nextSemester = nextSemester;
        this.nextYear = nextYear;
        this.enrollmentDate = LocalDateTime.now();
        this.enrolledBy = enrolledBy;
        this.status = "Confirmed";
        this.remarks = "";
    }
    
    /**
     * Default constructor
     */
    public Enrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.status = "Pending";
    }
    
    // Getters and Setters
    public String getEnrollmentId() {
        return enrollmentId;
    }
    
    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getNextSemester() {
        return nextSemester;
    }
    
    public void setNextSemester(String nextSemester) {
        this.nextSemester = nextSemester;
    }
    
    public String getNextYear() {
        return nextYear;
    }
    
    public void setNextYear(String nextYear) {
        this.nextYear = nextYear;
    }
    
    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public String getEnrolledBy() {
        return enrolledBy;
    }
    
    public void setEnrolledBy(String enrolledBy) {
        this.enrolledBy = enrolledBy;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getFormattedEnrollmentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return enrollmentDate.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("Enrollment{ID='%s', Student='%s', Next='%s %s', Status='%s'}",
                enrollmentId, studentId, nextSemester, nextYear, status);
    }
}