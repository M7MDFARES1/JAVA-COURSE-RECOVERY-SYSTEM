package crs.model;


public class Assessment {

  

    private String courseId;
    private String type;           // Exam, Assignment, Quiz...
    private double weightPercent;  // e.g. 70 for 70%
    private double score;          // student's raw score
    private double maxScore;       // maximum marks

    
    public Assessment(String courseId,
                      String type,
                      double weightPercent,
                      double score,
                      double maxScore) {

        this.courseId = courseId;
        this.type = type;
        this.weightPercent = weightPercent;
        this.score = score;
        this.maxScore = maxScore;
    }


    public String getCourseId() { return courseId; }
    public String getType() { return type; }
    public double getWeightPercent() { return weightPercent; }
    public double getScore() { return score; }
    public double getMaxScore() { return maxScore; }



 
    public double computeWeightedMark() {
        if (maxScore == 0) return 0;
        return (score / maxScore) * weightPercent;
    }

    @Override
    public String toString() {
        return type + " (" + weightPercent + "%)";
    }

   
}
