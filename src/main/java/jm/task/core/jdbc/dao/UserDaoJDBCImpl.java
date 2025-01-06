package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.conn;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Statement statement;
        Util.connect();
        try {
            if (isExist()) {
                System.out.println(
                        "The table you are trying to create is already exists");
                Util.unconnect();
            } else {
                statement = conn.createStatement();

                String SQL = "CREATE TABLE user_s " +
                        "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        " name VARCHAR(50), " +
                        " lastName VARCHAR (50), " +
                        " age TINYINT UNSIGNED not NULL)";
                statement.executeUpdate(SQL);
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public void dropUsersTable() {
        Statement statement;
        Util.connect();
        try {
            if (isExist()) {
                statement = conn.createStatement();
                String SQL = "DROP TABLE user_s";
                statement.executeUpdate(SQL);
                conn.close();
            } else {
                System.out.println(
                        "The table you are trying to delete is not exists");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Statement statement;
        Util.connect();
        try {
            if (!isExist()) {
                createUsersTable();
            }
            statement = conn.createStatement();
            statement.executeUpdate("INSERT user_s(name, lastName, age) VALUES ('" + name + "', '" + lastName + "', '" + age + "')");
            System.out.printf("User с именем — %s добавлен в базу данных \n", name);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public void removeUserById(long id) {
        Statement statement;
        Util.connect();
        String SQL;
        try {
            statement = conn.createStatement();
            SQL = "DELETE FROM user_s WHERE id = '" + id + "'";
            statement.executeUpdate(SQL);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Statement statement;
        Util.connect();
        try {
            if (isExist()) {
                statement = conn.createStatement();
                String SQL = "SELECT * FROM user_s";
                ResultSet resultSet = statement.executeQuery(SQL);
                while (resultSet.next()) {
                    User u = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getByte(4));
                    u.setId(resultSet.getLong(1));
                    users.add(u);
                }
            } else {
                System.out.println(
                        "The table you are trying to receive data is not exists");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
        return users;
    }

    public void cleanUsersTable() {
        Statement statement;
        Util.connect();
        String SQL;
        try {
            statement = conn.createStatement();

            SQL = "DELETE FROM user_s";
            statement.executeUpdate(SQL);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    private boolean isExist() {
        PreparedStatement checkIfTableExists = null;
        try {
            checkIfTableExists = conn.prepareStatement("SELECT count(*) FROM information_schema.tables WHERE table_name = 'user_s' LIMIT 1");

            ResultSet resultSet = checkIfTableExists.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
