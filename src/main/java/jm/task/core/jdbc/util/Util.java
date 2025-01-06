package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/MyPPDB";
    private static final String USER = "root";
    private static final String PASSWD = "root";
    public static Connection conn;
    static Driver driver;

    public static void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWD);
//            driver = new com.mysql.cj.jdbc.Driver(); (у меня и без этого работает)
        } catch (SQLException e) {
            Logger.getLogger(Util.class.getName()).info("failed connection attempt!");
        }
    }

    public static void unconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(Util.class.getName()).info("connection non closed!");
        }
    }
}
