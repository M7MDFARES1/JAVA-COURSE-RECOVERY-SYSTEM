
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crs;
/**
 *
 * @author konda
 */

// This class represents ONE recovery step in the plan
public class RecoveryTask {

    // I keep everything public to make life easy
    public String courseId;
    public String week;
    public String description;
    public String status;

    // this willTrim all of the input to remove extra spaces
    public RecoveryTask(String courseId, String week, String description, String status) {
        this.courseId = (courseId == null ? "" : courseId.trim());
        this.week = (week == null ? "" : week.trim());
        this.description = (description == null ? "" : description.trim());
        // to show a Default status if its emptyy
         if (status == null || status.trim().isEmpty()) {
            this.status = "Not Started";
        } else {
            this.status = status.trim();
        }
    }
    //this is to check if the course and week and des is filled and will return T OR F
     public boolean isValid() {
        return !courseId.isEmpty()
                && !week.isEmpty()
                && !description.isEmpty();
    }

    //this will Just print the task nicely
    @Override
    public String toString() {
        return courseId + " | " + week + " | " + description + " | " + status;
    }
}
