package com.mycompany.crs.service;

import com.mycompany.crs.model.Student;
import com.mycompany.crs.model.Enrollment;
import com.mycompany.crs.util.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * EligibilityService - Business logic for Eligibility Check & Enrollment
 * 
 * OOP Concepts Demonstrated:
 * - Abstraction: Hides complexity of eligibility logic
 * - Encapsulation: Private helper methods
 * - Modularity: Focused on eligibility operations only
 * - Single Responsibility Principle
 * 
 * This is YOUR MODULE - handles:
 * 1. Checking student eligibility
 * 2. Listing ineligible students
 * 3. Processing enrollments
 * 
 * @author YourName
 * @version 1.0
 */
public class EligibilityService {
    
    // Constants from assignment requirements
    private static final double MIN_CGPA = 2.0;
    private static final int MAX_FAILED_COURSES = 3;
    
    private FileManager fileManager;
    
    /**
     * Constructor
     */
    public EligibilityService() {
        this.fileManager = new FileManager();
    }
    
    /**
     * Get all students from the system
     * 
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return fileManager.loadStudents();
    }
    
    /**
     * Get students who are NOT eligible for progression
     * 
     * THIS IS YOUR CORE REQUIREMENT!
     * "List out all students who are not eligible to progress into next level"
     * 
     * Eligibility criteria:
     * - At least CGPA 2.0
     * - Not more than 3 failed courses
     * 
     * @return List of ineligible students
     */
    public List<Student> getIneligibleStudents() {
        List<Student> allStudents = getAllStudents();
        List<Student> ineligibleStudents = new ArrayList<>();
        
        for (Student student : allStudents) {
            // Check eligibility
            student.checkEligibility();
            
            if (!student.isEligible()) {
                ineligibleStudents.add(student);
            }
        }
        
        System.out.println("Found " + ineligibleStudents.size() + " ineligible students");
        return ineligibleStudents;
    }
    
    /**
     * Get students who ARE eligible for progression
     * 
     * @return List of eligible students
     */
    public List<Student> getEligibleStudents() {
        List<Student> allStudents = getAllStudents();
        List<Student> eligibleStudents = new ArrayList<>();
        
        for (Student student : allStudents) {
            student.checkEligibility();
            
            if (student.isEligible()) {
                eligibleStudents.add(student);
            }
        }
        
        System.out.println("Found " + eligibleStudents.size() + " eligible students");
        return eligibleStudents;
    }
    
    /**
     * Get student by ID
     * 
     * @param studentId Student ID
     * @return Student object or null
     */
    public Student getStudentById(String studentId) {
        List<Student> students = getAllStudents();
        
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        
        return null;
    }
    
    /**
     * Check if a specific student is eligible
     * 
     * @param studentId Student ID
     * @return true if eligible
     */
    public boolean checkStudentEligibility(String studentId) {
        Student student = getStudentById(studentId);
        
        if (student == null) {
            System.err.println("Student not found: " + studentId);
            return false;
        }
        
        return student.checkEligibility();
    }
    
    /**
     * Get detailed eligibility information
     * 
     * @param studentId Student ID
     * @return Array [CGPA, Failed Count, Is Eligible, Reason]
     */
    public Object[] getEligibilityDetails(String studentId) {
        Student student = getStudentById(studentId);
        
        if (student == null) {
            return new Object[]{0.0, 0, false, "Student not found"};
        }
        
        double cgpa = student.calculateCGPA();
        int failedCourses = student.countFailedCourses();
        boolean isEligible = student.checkEligibility();
        String reason = student.getEligibilityReason();
        
        return new Object[]{cgpa, failedCourses, isEligible, reason};
    }
    
    /**
     * Process enrollment for eligible student
     * 
     * "To allow registration once the eligibility is confirmed"
     * 
     * @param studentId Student ID
     * @param nextSemester Next semester (Spring/Summer/Fall)
     * @param nextYear Next year level
     * @param enrolledBy Officer processing enrollment
     * @return Enrollment object if successful, null otherwise
     */
    public Enrollment processEnrollment(String studentId, String nextSemester, 
                                       String nextYear, String enrolledBy) {
        Student student = getStudentById(studentId);
        
        // Validate student exists
        if (student == null) {
            System.err.println("✗ Error: Student not found");
            return null;
        }
        
        // Check eligibility
        if (!student.checkEligibility()) {
            System.err.println("✗ Error: Student is not eligible for enrollment");
            System.err.println("  Reason: " + student.getEligibilityReason());
            return null;
        }
        
        // Check if already enrolled
        if ("Enrolled".equals(student.getEnrollmentStatus())) {
            System.err.println("✗ Error: Student is already enrolled for next semester");
            return null;
        }
        
        // Validate enrollment data
        if (!validateEnrollmentData(nextSemester, nextYear)) {
            System.err.println("✗ Error: Invalid enrollment data");
            return null;
        }
        
        // Generate unique enrollment ID
        String enrollmentId = generateEnrollmentId(studentId);
        
        // Create enrollment record
        Enrollment enrollment = new Enrollment(
            enrollmentId,
            studentId,
            nextSemester,
            nextYear,
            enrolledBy
        );
        enrollment.setStatus("Confirmed");
        enrollment.setRemarks("Enrolled for progression to next level");
        
        // Update student status
        student.setEnrollmentStatus("Enrolled");
        fileManager.updateStudent(student);
        
        // Save enrollment record
        List<Enrollment> enrollments = fileManager.loadEnrollments();
        enrollments.add(enrollment);
        fileManager.saveEnrollments(enrollments);
        
        System.out.println("✓ Successfully enrolled student: " + studentId);
        return enrollment;
    }
    
    /**
     * Get enrollment history for a student
     * 
     * @param studentId Student ID
     * @return List of enrollments
     */
    public List<Enrollment> getStudentEnrollments(String studentId) {
        List<Enrollment> allEnrollments = fileManager.loadEnrollments();
        List<Enrollment> studentEnrollments = new ArrayList<>();
        
        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getStudentId().equals(studentId)) {
                studentEnrollments.add(enrollment);
            }
        }
        
        return studentEnrollments;
    }
    
    /**
     * Get eligibility statistics
     * 
     * @return Array [Total, Eligible, Ineligible, Eligibility Rate %]
     */
    public Object[] getEligibilityStatistics() {
        List<Student> allStudents = getAllStudents();
        List<Student> eligible = getEligibleStudents();
        List<Student> ineligible = getIneligibleStudents();
        
        int total = allStudents.size();
        int eligibleCount = eligible.size();
        int ineligibleCount = ineligible.size();
        
        double eligibilityRate = total > 0 ? (eligibleCount * 100.0 / total) : 0.0;
        eligibilityRate = Math.round(eligibilityRate * 100.0) / 100.0;
        
        return new Object[]{total, eligibleCount, ineligibleCount, eligibilityRate};
    }
    
    /**
     * Validate enrollment data
     * 
     * @param nextSemester Semester name
     * @param nextYear Year level
     * @return true if valid
     */
    private boolean validateEnrollmentData(String nextSemester, String nextYear) {
        if (nextSemester == null || nextSemester.trim().isEmpty()) {
            return false;
        }
        
        if (nextYear == null || nextYear.trim().isEmpty()) {
            return false;
        }
        
        // Validate semester
        if (!nextSemester.equals("Spring") && !nextSemester.equals("Summer") && 
            !nextSemester.equals("Fall")) {
            return false;
        }
        
        // Validate year
        if (!nextYear.equals("Freshman") && !nextYear.equals("Sophomore") && 
            !nextYear.equals("Junior") && !nextYear.equals("Senior")) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Generate unique enrollment ID
     * Format: ENR-{StudentID}-{Count}
     * 
     * @param studentId Student ID
     * @return Generated enrollment ID
     */
    private String generateEnrollmentId(String studentId) {
        List<Enrollment> enrollments = fileManager.loadEnrollments();
        int count = 0;
        
        // Count existing enrollments for this student
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId)) {
                count++;
            }
        }
        
        return String.format("ENR-%s-%03d", studentId, count + 1);
    }
    
    /**
     * Refresh student eligibility status
     * Recalculates eligibility for all students
     */
    public void refreshEligibilityStatus() {
        List<Student> students = getAllStudents();
        
        for (Student student : students) {
            student.checkEligibility();
        }
        
        fileManager.saveStudents(students);
        System.out.println("✓ Refreshed eligibility status for all students");
    }
}