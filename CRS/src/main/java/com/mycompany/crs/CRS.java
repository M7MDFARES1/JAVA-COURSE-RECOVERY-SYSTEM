package com.mycompany.crs;

import crs.users.UserManager;
import crs.gui.LoginForm;

public class CRS {
    public static void main(String[] args) {
        UserManager manager = new UserManager();
        manager.loadUsers();

        LoginForm login = new LoginForm(manager);
        login.setVisible(true);
    }
}
