package crs.users;

import crs.file.UserFileHandler;
import java.util.ArrayList;

public class UserManager {

    public ArrayList<User> users = new ArrayList<>();

    // Add a new user
    public void addUser(User u) {
        users.add(u);
        saveUsers(); // save immediately
    }

    // Update an existing user (search by email)
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).email.equals(updatedUser.email)) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveUsers();
    }

    // Deactivate user by email
    public void deactivateUser(String email) {
        for (User u : users) {
            if (u.email.equals(email)) {
                u.status = "deactivated";
                break;
            }
        }
        saveUsers();
    }

    // Login check
    public User login(String email, String password) {
        for (User u : users) {
            if (u.email.equals(email) && u.password.equals(password)) {
                return u;
            }
        }
        return null; // login failed
    }

    // Load users from text file
    public void loadUsers() {
        users.clear();
        ArrayList<String> lines = UserFileHandler.readLines("users.txt");

        for (String line : lines) {
            User u = User.fromFileLine(line);
            if (u != null) users.add(u);
        }
    }

    // Save all users to file
    public void saveUsers() {
        ArrayList<String> lines = new ArrayList<>();

        for (User u : users) {
            lines.add(u.toFileLine());
        }

        UserFileHandler.writeLines("users.txt", lines);
    }
}
