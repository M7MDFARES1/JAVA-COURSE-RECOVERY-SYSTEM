package com.mycompany.crs.util;

import com.mycompany.crs.model.Student;
import com.mycompany.crs.model.Enrollment;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager - Handles binary file I/O operations
 * 
 * OOP Concepts Demonstrated:
 * - Encapsulation: Hides file operations complexity
 * - Modularity: Separate class for file management
 * - Exception Handling: Proper error management
 * 
 * As per requirement: "Use text or binary files for data access and manipulation only"
 * 
 * @author YourName
 * @version 1.0
 */
public class FileManager {
    
    // Binary file paths
    private static final String STUDENTS_FILE = "data/students.dat";
    private static final String ENROLLMENTS_FILE = "data/enrollments.dat";
    private static final String DATA_DIR = "data";
    
    /**
     * Constructor - ensures data directory exists
     */
    public FileManager() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    
    /**
     * Save students to binary file
     * 
     * @param students List of students
     * @return true if successful
     */
    public boolean saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
            System.out.println("✓ Students saved successfully to " + STUDENTS_FILE);
            return true;
        } catch (IOException e) {
            System.err.println("✗ Error saving students: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load students from binary file
     * 
     * @return List of students
     */
    @SuppressWarnings("unchecked")
    public List<Student> loadStudents() {
        File file = new File(STUDENTS_FILE);
        
        // If file doesn't exist, create initial data
        if (!file.exists()) {
            System.out.println("ℹ No existing student data. Creating initial data...");
            DataInitializer initializer = new DataInitializer();
            List<Student> students = initializer.getStudents();
            saveStudents(students);
            return students;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STUDENTS_FILE))) {
            List<Student> students = (List<Student>) ois.readObject();
            System.out.println("✓ Loaded " + students.size() + " students from file");
            return students;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error loading students: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Save enrollments to binary file
     * 
     * @param enrollments List of enrollments
     * @return true if successful
     */
    public boolean saveEnrollments(List<Enrollment> enrollments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ENROLLMENTS_FILE))) {
            oos.writeObject(enrollments);
            System.out.println("✓ Enrollments saved successfully");
            return true;
        } catch (IOException e) {
            System.err.println("✗ Error saving enrollments: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load enrollments from binary file
     * 
     * @return List of enrollments
     */
    @SuppressWarnings("unchecked")
    public List<Enrollment> loadEnrollments() {
        File file = new File(ENROLLMENTS_FILE);
        
        if (!file.exists()) {
            System.out.println("ℹ No existing enrollment records");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ENROLLMENTS_FILE))) {
            List<Enrollment> enrollments = (List<Enrollment>) ois.readObject();
            System.out.println("✓ Loaded " + enrollments.size() + " enrollment records");
            return enrollments;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error loading enrollments: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Update a specific student in the file
     * 
     * @param updatedStudent Student to update
     * @return true if successful
     */
    public boolean updateStudent(Student updatedStudent) {
        List<Student> students = loadStudents();
        boolean found = false;
        
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(updatedStudent.getStudentId())) {
                students.set(i, updatedStudent);
                found = true;
                break;
            }
        }
        
        if (found) {
            return saveStudents(students);
        }
        
        System.err.println("✗ Student not found: " + updatedStudent.getStudentId());
        return false;
    }
    
    /**
     * Reset all data - for testing purposes
     */
    public void resetAllData() {
        File studentsFile = new File(STUDENTS_FILE);
        File enrollmentsFile = new File(ENROLLMENTS_FILE);
        
        if (studentsFile.exists()) {
            studentsFile.delete();
            System.out.println("✓ Deleted old student data");
        }
        
        if (enrollmentsFile.exists()) {
            enrollmentsFile.delete();
            System.out.println("✓ Deleted old enrollment data");
        }
        
        // Recreate fresh data
        DataInitializer initializer = new DataInitializer();
        saveStudents(initializer.getStudents());
        System.out.println("✓ Fresh data created");
    }
}