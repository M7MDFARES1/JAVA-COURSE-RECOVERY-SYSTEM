package crs.model;

import java.util.ArrayList;
import java.util.List;

public class RecoveryPlan {

    private String studentId;
    private List<RecoveryTask> tasks;

    public RecoveryPlan(String studentId) {
        this.studentId = studentId;
        this.tasks = new ArrayList<>();
    }

    public void addTask(RecoveryTask task) { tasks.add(task); }
    public List<RecoveryTask> getTasks() { return tasks; }
}
