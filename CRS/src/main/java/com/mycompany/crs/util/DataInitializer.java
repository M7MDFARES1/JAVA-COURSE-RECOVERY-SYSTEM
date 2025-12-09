package com.mycompany.crs.util;

import com.mycompany.crs.model.*;
import java.util.*;

/**
 * DataInitializer - Loads from CSV files with realistic grade distribution
 * 
 * 65 ELIGIBLE students (CGPA 2.0-4.0, varied)
 * 35 INELIGIBLE students (CGPA 0.9-1.9, varied)
 * 
 * @author YourName
 * @version 5.0 - CSV VERSION
 */
public class DataInitializer {
    
    private static final String STUDENTS_CSV = "data/student_information 2.csv";
    private static final String COURSES_CSV = "data/course_assessment_information 2.csv";
    
    private Map<String, Course> coursesMap;
    private List<Student> studentsList;
    private Random rand;
    
    public DataInitializer() {
        coursesMap = new HashMap<>();
        studentsList = new ArrayList<>();
        rand = new Random(42); // Fixed seed for consistency
        
        System.out.println("========================================");
        System.out.println("  LOADING DATA FROM CSV FILES");
        System.out.println("========================================");
        
        loadCoursesFromCSV();
        loadStudentsFromCSV();
        assignRealisticGrades();
        
        System.out.println("========================================");
        System.out.println("  DATA LOADING COMPLETE!");
        System.out.println("  Total Students: " + studentsList.size());
        System.out.println("  Total Courses: " + coursesMap.size());
        System.out.println("========================================");
    }
    
    /**
     * Load courses from CSV file
     */
    private void loadCoursesFromCSV() {
        System.out.println("\n📚 Loading courses from CSV...");
        
        List<Map<String, String>> courseData = CSVReader.readCSV(COURSES_CSV);
        
        for (Map<String, String> row : courseData) {
            String courseId = row.get("CourseID");
            String courseName = row.get("CourseName");
            int credits = CSVReader.parseInt(row.get("Credits"), 3);
            String semester = row.get("Semester");
            String instructor = row.get("Instructor");
            int examWeight = CSVReader.parseInt(row.get("ExamWeight"), 60);
            int assignmentWeight = CSVReader.parseInt(row.get("AssignmentWeight"), 40);
            
            Course course = new Course(courseId, courseName, credits, semester, 
                                      instructor, examWeight, assignmentWeight);
            coursesMap.put(courseId, course);
        }
        
        System.out.println("✓ Loaded " + coursesMap.size() + " courses");
    }
    
    /**
     * Load students from CSV file
     */
    private void loadStudentsFromCSV() {
        System.out.println("\n👥 Loading students from CSV...");
        
        List<Map<String, String>> studentData = CSVReader.readCSV(STUDENTS_CSV);
        
        for (Map<String, String> row : studentData) {
            String studentId = row.get("StudentID");
            String firstName = row.get("FirstName");
            String lastName = row.get("LastName");
            String major = row.get("Major");
            String year = row.get("Year");
            String email = row.get("Email");
            
            Student student = new Student(studentId, firstName, lastName, 
                                         major, year, email);
            studentsList.add(student);
        }
        
        System.out.println("✓ Loaded " + studentsList.size() + " students");
    }
    
    /**
     * Assign realistic grades to students
     * 65 ELIGIBLE (varied CGPAs 2.0-4.0)
     * 35 INELIGIBLE (varied CGPAs 0.9-1.9)
     */
    private void assignRealisticGrades() {
        System.out.println("\n📊 Assigning realistic grades...");
        
        // Shuffle students for random distribution
        Collections.shuffle(studentsList, rand);
        
        // First 65 students will be ELIGIBLE
        // Last 35 students will be INELIGIBLE
        
        List<String> courseIdList = new ArrayList<>(coursesMap.keySet());
        
        int eligibleCount = 0;
        int ineligibleCount = 0;
        
        for (int i = 0; i < studentsList.size(); i++) {
            Student student = studentsList.get(i);
            
            boolean makeEligible = (i < 65); // First 65 are eligible
            
            // Each student takes 4-5 courses
            int numCourses = 4 + rand.nextInt(2);
            
            // Select random courses
            Set<String> selectedCourses = new HashSet<>();
            while (selectedCourses.size() < numCourses) {
                String courseId = courseIdList.get(rand.nextInt(courseIdList.size()));
                selectedCourses.add(courseId);
            }
            
            if (makeEligible) {
                assignEligibleGrades(student, selectedCourses);
                eligibleCount++;
            } else {
                assignIneligibleGrades(student, selectedCourses);
                ineligibleCount++;
            }
            
            // Check eligibility
            student.checkEligibility();
            
            // Randomly enroll some eligible students (15%)
            if (student.isEligible() && rand.nextDouble() < 0.15) {
                student.setEnrollmentStatus("Enrolled");
            }
        }
        
        System.out.println("✓ Grade assignment complete!");
        System.out.println("  - Eligible students: " + eligibleCount);
        System.out.println("  - Ineligible students: " + ineligibleCount);
    }
    
    /**
     * Assign grades for ELIGIBLE student (CGPA 2.0-4.0)
     * Varied realistic distribution
     */
    private void assignEligibleGrades(Student student, Set<String> courseIds) {
        // Target CGPA range: 2.0 to 4.0
        // Distribution: 20% (2.0-2.5), 30% (2.5-3.0), 30% (3.0-3.5), 20% (3.5-4.0)
        
        double targetCGPA;
        double roll = rand.nextDouble();
        
        if (roll < 0.20) {
            targetCGPA = 2.0 + (rand.nextDouble() * 0.5); // 2.0-2.5
        } else if (roll < 0.50) {
            targetCGPA = 2.5 + (rand.nextDouble() * 0.5); // 2.5-3.0
        } else if (roll < 0.80) {
            targetCGPA = 3.0 + (rand.nextDouble() * 0.5); // 3.0-3.5
        } else {
            targetCGPA = 3.5 + (rand.nextDouble() * 0.5); // 3.5-4.0
        }
        
        // Allow 0-2 failed courses (still eligible if ≤3)
        int failedCourses = rand.nextInt(3); // 0, 1, or 2
        
        int courseIndex = 0;
        for (String courseId : courseIds) {
            Course course = coursesMap.get(courseId);
            if (course == null) continue;
            
            int examScore, assignmentScore;
            
            if (courseIndex < failedCourses) {
                // Failed course (total 50-59 or below)
                examScore = 20 + rand.nextInt(25); // 20-44
                assignmentScore = 15 + rand.nextInt(20); // 15-34
            } else {
                // Passing grade based on target CGPA
                if (targetCGPA >= 3.5) {
                    // A/A- grades (90-100 total)
                    examScore = 55 + rand.nextInt(15); // 55-69
                    assignmentScore = 35 + rand.nextInt(26); // 35-60
                } else if (targetCGPA >= 3.0) {
                    // B+/B grades (75-89 total)
                    examScore = 45 + rand.nextInt(20); // 45-64
                    assignmentScore = 30 + rand.nextInt(25); // 30-54
                } else if (targetCGPA >= 2.5) {
                    // B-/C+ grades (65-74 total)
                    examScore = 35 + rand.nextInt(20); // 35-54
                    assignmentScore = 28 + rand.nextInt(22); // 28-49
                } else {
                    // C grades (60-64 total)
                    examScore = 30 + rand.nextInt(15); // 30-44
                    assignmentScore = 28 + rand.nextInt(18); // 28-45
                }
            }
            
            CourseEnrollment enrollment = new CourseEnrollment(course, examScore, assignmentScore);
            student.addCourseEnrollment(enrollment);
            
            courseIndex++;
        }
    }
    
    /**
     * Assign grades for INELIGIBLE student (CGPA 0.9-1.9)
     * Varied realistic distribution
     */
    private void assignIneligibleGrades(Student student, Set<String> courseIds) {
        // Target CGPA range: 0.9 to 1.9
        // Distribution: evenly spread
        
        double targetCGPA = 0.9 + (rand.nextDouble() * 1.0); // 0.9-1.9
        
        // Most courses should be failed (4-5 failures to ensure ineligibility)
        int totalCourses = courseIds.size();
        int failedCourses = Math.max(4, totalCourses - rand.nextInt(2)); // At least 4 failed
        
        int courseIndex = 0;
        for (String courseId : courseIds) {
            Course course = coursesMap.get(courseId);
            if (course == null) continue;
            
            int examScore, assignmentScore;
            
            if (courseIndex < failedCourses) {
                // Failed course - vary the scores for realism
                if (targetCGPA < 1.2) {
                    // Very low scores (15-35 total)
                    examScore = 10 + rand.nextInt(15); // 10-24
                    assignmentScore = 8 + rand.nextInt(15); // 8-22
                } else if (targetCGPA < 1.5) {
                    // Low scores (30-45 total)
                    examScore = 18 + rand.nextInt(18); // 18-35
                    assignmentScore = 12 + rand.nextInt(18); // 12-29
                } else {
                    // Close to passing (45-55 total)
                    examScore = 25 + rand.nextInt(20); // 25-44
                    assignmentScore = 18 + rand.nextInt(20); // 18-37
                }
            } else {
                // Occasional passing grade
                examScore = 35 + rand.nextInt(25); // 35-59
                assignmentScore = 25 + rand.nextInt(25); // 25-49
            }
            
            CourseEnrollment enrollment = new CourseEnrollment(course, examScore, assignmentScore);
            student.addCourseEnrollment(enrollment);
            
            courseIndex++;
        }
    }
    
    public List<Student> getStudents() {
        return new ArrayList<>(studentsList);
    }
    
    public List<Course> getCourses() {
        return new ArrayList<>(coursesMap.values());
    }
}