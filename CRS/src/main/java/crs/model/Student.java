package crs.model;

import java.util.ArrayList;
import java.util.List;

public class Student {

    //======================================
    // Fields
    //======================================
    private String studentId;
    private String firstName;
    private String lastName;
    private String major;
    private String studyYear;
    private String email;

    private List<CourseResult> courseResults;
    private RecoveryPlan recoveryPlan;

    //======================================
    // Constructor
    //======================================
    public Student(String studentId, String firstName, String lastName,
                   String major, String studyYear, String email) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.studyYear = studyYear;
        this.email = email;

        this.courseResults = new ArrayList<>();
        this.recoveryPlan = new RecoveryPlan(studentId);
    }

    //======================================
    // Getters
    //======================================
    public String getStudentId() { return studentId; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getMajor() { return major; }
    public String getStudyYear() { return studyYear; }
    public String getEmail() { return email; }
    public List<CourseResult> getCourseResults() { return courseResults; }
    public RecoveryPlan getRecoveryPlan() { return recoveryPlan; }

    //======================================
    // Methods for adding data
    //======================================
    public void addCourseResult(CourseResult result) {
        courseResults.add(result);
    }

    public void addRecoveryTask(RecoveryTask task) {
        recoveryPlan.addTask(task);
    }

    @Override
    public String toString() {
        return studentId + " - " + getFullName();
    }
}
