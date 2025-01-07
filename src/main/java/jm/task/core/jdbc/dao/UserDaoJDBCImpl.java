package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static jm.task.core.jdbc.util.Util.conn;

public class UserDaoJDBCImpl implements UserDao {
    public static Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Util.connect();
        try {
            if (isExist()) {
                logger.info("The table you are trying to create is already exists \n");
            } else {
                String SQL = "CREATE TABLE user_s " +
                        "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        " name VARCHAR(50), " +
                        " lastName VARCHAR (50), " +
                        " age TINYINT UNSIGNED not NULL)";
                PreparedStatement stmt = conn.prepareStatement(SQL);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public void dropUsersTable() {
        Util.connect();
        try {
            if (isExist()) {
                String SQL = "DROP TABLE user_s";
                PreparedStatement stmt = conn.prepareStatement(SQL);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
            } else {
                logger.info("The table you are trying to delete is not exists");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Util.connect();
        try {
            if (!isExist()) {
                createUsersTable();
            }
            String SQL = "INSERT user_s(name, lastName, age) VALUES ('" + name + "', '" + lastName + "', '" + age + "')";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            logger.info(String.format("User с именем — %s добавлен в базу данных \n", name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public void removeUserById(long id) {
        Util.connect();
        try {
            String SQL = "DELETE FROM user_s WHERE id = '" + id + "'";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Util.connect();
        try {
            if (isExist()) {
                String SQL = "SELECT * FROM user_s";
                PreparedStatement stmt = conn.prepareStatement(SQL);
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    User u = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getByte(4));
                    u.setId(resultSet.getLong(1));
                    users.add(u);
                }
                resultSet.close();
                stmt.close();
                conn.close();
            } else {
                logger.info("The table you are trying to receive data is not exists\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Util.unconnect();
        }
        return users;
    }

    public void cleanUsersTable() {
        Util.connect();
        try {
            String SQL = "DELETE FROM user_s";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
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
