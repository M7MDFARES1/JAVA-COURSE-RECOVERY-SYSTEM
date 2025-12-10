package crs.model;

public class Course {

    private String courseId;
    private String courseName;
    private int credits;
    private String semester;
    private String instructor;
    private int examWeight;
    private int assignmentWeight;

    public Course(String courseId, String courseName, int credits,
                  String semester, String instructor,
                  int examWeight, int assignmentWeight) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.instructor = instructor;
        this.examWeight = examWeight;
        this.assignmentWeight = assignmentWeight;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getSemester() { return semester; }
    public String getInstructor() { return instructor; }
    public int getExamWeight() { return examWeight; }
    public int getAssignmentWeight() { return assignmentWeight; }
}
