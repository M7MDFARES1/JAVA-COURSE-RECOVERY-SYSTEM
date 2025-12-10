package crs.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CourseRegistry {

    private static Map<String, Course> map = new HashMap<>();

    public static void addCourse(Course c) { 
        map.put(c.getCourseId(), c); 
    }

    public static Course getCourse(String id) { 
        return map.get(id); 
    }


    public static Collection<Course> getAllCourses() {
        return map.values();
    }
}
