package crs.model;

import java.util.List;


public class EligibilitySummary {

   
    private boolean eligible;                 // true = eligible, false = not eligible
    private String message;                   // explanation (e.g., "CGPA below 2.0")
    private List<CourseResult> failedCourses; // list of failed courses


   
    public EligibilitySummary(boolean eligible,
                              String message,
                              List<CourseResult> failedCourses) {

        this.eligible = eligible;
        this.message = message;
        this.failedCourses = failedCourses;
    }



    public boolean isEligible() {
        return eligible;
    }

    public String getMessage() {
        return message;
    }

    public List<CourseResult> getFailedCourses() {
        return failedCourses;
    }


   
    @Override
    public String toString() {
        return (eligible ? "Eligible" : "Not Eligible") + " - " + message;
    }
}
