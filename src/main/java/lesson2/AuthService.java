package lesson2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> clients;
    private Connection databaseConnection;

    public AuthService()  {
        clients = new ArrayList<>();
        try {
            this.databaseConnection = DatabaseConnection.getInstance().getConnection();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean addUserToDb(String name, String login, String password) {
        try {
            Statement statement = this.databaseConnection.createStatement();
            String insert = "INSERT INTO user (name, login, password) VALUES (\"%s\",\"%s\", \"%s\")";
            statement.executeUpdate(String.format(insert, name, login, password));
            this.clients.add(new User(name, login, password));
            return true;
        } catch (SQLException ex) {
            System.out.println("Не удалось добавить нового пользователя, "+ ex.getMessage());
            return false;
        }
    }



    synchronized  public User signIn (String login, String password) {
        for (User client : this.clients) {
            if (client.getLogin().equals(login) && client.getPassword().equals(password)) {
                return client ;
            }
        }
        return null;
    }
}
