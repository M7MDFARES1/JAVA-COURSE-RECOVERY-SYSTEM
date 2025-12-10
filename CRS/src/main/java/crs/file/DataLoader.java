package crs.file;

import crs.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DataLoader {

    // ==========================================================
    // LOAD STUDENTS (CSV)
    // ==========================================================
    public static List<Student> loadStudents(String path) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");

                if (p.length != 6) {
                    System.out.println("Skipping line (columns = " + p.length + "): " + line);
                    continue;
                }

                Student s = new Student(
                        p[0].trim(), // ID
                        p[1].trim(), // First Name
                        p[2].trim(), // Last Name
                        p[3].trim(), // Major
                        p[4].trim(), // Year
                        p[5].trim()  // Email
                );

                students.add(s);
            }

            System.out.println("[DataLoader] Loaded Students: " + students.size());

        } catch (Exception e) {
            System.err.println("❌ Error loading students from: " + path);
            e.printStackTrace();
        }

        return students;
    }

    // ==========================================================
    // LOAD COURSES (CSV)
    // ==========================================================
    public static void loadCourses(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");

                if (p.length < 7) {
                    System.out.println("Skipping course line: " + line);
                    continue;
                }

                Course c = new Course(
                        p[0].trim(),               // course ID
                        p[1].trim(),               // course name
                        Integer.parseInt(p[2].trim()), // credit hours
                        p[3].trim(),               // type
                        p[4].trim(),               // department
                        Integer.parseInt(p[5].trim()), // min score
                        Integer.parseInt(p[6].trim())  // max score
                );

                CourseRegistry.addCourse(c);
            }

            System.out.println("[DataLoader] Loaded Courses: " + CourseRegistry.getAllCourses().size());

        } catch (Exception e) {
            System.err.println("❌ Error loading courses from: " + path);
            e.printStackTrace();
        }
    }

    // ==========================================================
    // LOAD COURSE RESULTS (CSV)
    // ==========================================================
    public static void loadResults(String path, List<Student> students) {

        Map<String, Student> studentMap = new HashMap<>();
        for (Student s : students) studentMap.put(s.getStudentId(), s);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Skip header
            int count = 0;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");

                if (p.length < 8) {
                    System.out.println("Skipping result line: " + line);
                    continue;
                }

                String studentId = p[0].trim();
                String courseId = p[1].trim();
                double examScore = Double.parseDouble(p[2].trim());
                double assignmentScore = Double.parseDouble(p[3].trim());
                double finalMark = Double.parseDouble(p[4].trim());
                String grade = p[5].trim();
                double gradePoint = Double.parseDouble(p[6].trim());
                boolean passed = Boolean.parseBoolean(p[7].trim());

                CourseResult result = new CourseResult(
                        courseId, examScore, assignmentScore,
                        finalMark, grade, gradePoint, passed
                );

                if (studentMap.containsKey(studentId)) {
                    studentMap.get(studentId).addCourseResult(result);
                    count++;
                }
            }

            System.out.println("[DataLoader] Loaded Course Results: " + count);

        } catch (Exception e) {
            System.err.println("❌ Error loading course results from: " + path);
            e.printStackTrace();
        }
    }
}
