package jm.task.core.jdbc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static Properties properties = new Properties();

    static {
        try (InputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fis);
            URL = properties.getProperty("db.host");
            USERNAME = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

//        try {
//            Driver driver = new com.mysql.cj.jdbc.Driver();
//            DriverManager.registerDriver(driver);
//        } catch (SQLException e) {
//            System.err.println(e.getMessage());
//        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
