package crs.users;

// This class represents ONE user in the system
public class User {
    public String name;
    public String email;
    public String password;
    public String role;   // "admin" or "student"
    public String status; // "active" or "deactivated"

    public User(String name, String email, String password, String role, String status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    // Convert user to a single line for file saving
    public String toFileLine() {
        return name + "," + email + "," + password + "," + role + "," + status;
    }

    // Create a user object from file line
    public static User fromFileLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null; // avoid crash

        return new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
public String getRole() {
    return role;
}

public String getStatus() {
    return status;
}
}
