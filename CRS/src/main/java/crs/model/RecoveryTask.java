package crs.model;

public class RecoveryTask {

    private String courseId;
    private String courseName;
    private String recoveryType;
    private String milestone;
    private String status;

    public RecoveryTask(String courseId, String courseName,
                        String recoveryType, String milestone,
                        String status) {

        this.courseId = courseId;
        this.courseName = courseName;
        this.recoveryType = recoveryType;
        this.milestone = milestone;
        this.status = status;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getRecoveryType() { return recoveryType; }
    public String getMilestone() { return milestone; }
    public String getStatus() { return status; }
}
