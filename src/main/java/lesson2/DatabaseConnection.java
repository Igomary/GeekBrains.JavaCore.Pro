package lesson2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url = "jdbc:sqlite:UsersDB.db";

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("Соединение с базой данных не установлено, "+e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }
}
