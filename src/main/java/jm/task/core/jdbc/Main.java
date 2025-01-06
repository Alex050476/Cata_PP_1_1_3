package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        new UserServiceImpl().createUsersTable();
        new UserServiceImpl().saveUser("Neymar", "jr", (byte) 35);
        new UserServiceImpl().saveUser("Mo", "Salah", (byte) 32);
        new UserServiceImpl().saveUser("Kilian", "Mbappe", (byte) 22);
        new UserServiceImpl().saveUser("Aleksandr", "Ovi", (byte) 39);
        List<User> users = new UserServiceImpl().getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
        new UserServiceImpl().removeUserById(3);
        new UserServiceImpl().cleanUsersTable();
        new UserServiceImpl().dropUsersTable();
    }
}
