/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crs;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author konda
 */
public class RecoveryPlan {

    public String studentId;
    public String courseId;
    public ArrayList<RecoveryTask> tasks;

    public RecoveryPlan(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.tasks = new ArrayList<>();
    }

    // make file name and folder (simple student way)
    private String getFileName() {
        File folder = new File("recovery_plans");
        if (!folder.exists()) {
            folder.mkdir(); // will just make the folder lol
        }
        return "recovery_plans/" + studentId + "_" + courseId + ".txt";
    }

    // check if task missing important stuff
    public boolean isValidTask(RecoveryTask t) {
        if (t == null) return false;
        if (t.courseId == null || t.courseId.equals("")) return false;
        if (t.week == null || t.week.equals("")) return false;
        if (t.description == null || t.description.equals("")) return false;
        return true;
    }

    // check if same week & description already exists
    public boolean taskExists(RecoveryTask t) {
        for (RecoveryTask x : tasks) {
            if (x.week.equalsIgnoreCase(t.week) &&
                x.description.equalsIgnoreCase(t.description)) {
                return true;
            }
        }
        return false;
    }

    // add task 
    public boolean addTask(RecoveryTask t) {
        if (!isValidTask(t)) {
            return false; // missing info
        }
        if (taskExists(t)) {
            return false; // duplicate
        }
        tasks.add(t);
        return true;
    }

    // update task in the list
    public boolean updateTask(int index, RecoveryTask t) {
        if (index < 0 || index >= tasks.size()) return false;
        if (!isValidTask(t)) return false;
        tasks.set(index, t);
        return true;
    }

    // remove a task
    public boolean deleteTask(int index) {
        if (index < 0 || index >= tasks.size()) return false;
        tasks.remove(index);
        return true;
    }

    // save everything to the file
    public void saveToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(getFileName()));

            for (RecoveryTask t : tasks) {
                out.println(t.courseId + "|" + t.week + "|" + t.description + "|" + t.status);
            }

            out.close();
        } catch (Exception e) {
            // i just printed it because idk how to handle errors yet
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // load everything from file
    public void loadFromFile() {
        tasks.clear();

        File file = new File(getFileName());
        if (!file.exists()) {
            return; // no file yet means no tasks
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";

            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 4) {
                    RecoveryTask t = new RecoveryTask(p[0], p[1], p[2], p[3]);
                    tasks.add(t);
                }
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
  
 