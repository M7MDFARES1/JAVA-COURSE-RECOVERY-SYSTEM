package crs.model;

public class CourseResult {

    private String courseId;
    private double examScore;
    private double assignmentScore;
    private double finalMark;
    private String grade;
    private double gradePoint;
    private boolean passed;

    public CourseResult(String courseId, double examScore,
                        double assignmentScore, double finalMark,
                        String grade, double gradePoint, boolean passed) {

        this.courseId = courseId;
        this.examScore = examScore;
        this.assignmentScore = assignmentScore;
        this.finalMark = finalMark;
        this.grade = grade;
        this.gradePoint = gradePoint;
        this.passed = passed;
    }

    public String getCourseId() { return courseId; }
    public double getExamScore() { return examScore; }
    public double getAssignmentScore() { return assignmentScore; }
    public double getFinalMark() { return finalMark; }
    public String getGrade() { return grade; }
    public double getGradePoint() { return gradePoint; }
    public boolean isPassed() { return passed; }
}
