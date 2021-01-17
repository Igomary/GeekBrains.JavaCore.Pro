package chat.server;

import java.sql.*;

public class DBHelper implements AutoCloseable {

    public static DBHelper instance;
    private static Connection connection;

    private DBHelper() {

    }

    public static DBHelper getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                loadDriverAndOpenConnection();
                instance = new DBHelper();
            }
            return instance;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection() {
        return DBHelper.connection;
    }

    private static void loadDriverAndOpenConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:UsersDB.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Соединение с базой данных не установлено, "+e.getMessage());
        }
    }

    public String findByLoginAndPassword(String login, String password) {
        String query = String.format("SELECT * FROM user WHERE LOWER(login) = LOWER(\"%s\") AND password = \"%s\"",login,password);

        System.out.println(query);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println(name);
                return name;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public int updateName(String oldName, String newName) {
        String query = String.format("UPDATE user SET name = \"%s\" WHERE name = \"%s\"", newName, oldName);

        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
