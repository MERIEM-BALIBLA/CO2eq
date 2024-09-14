package Repository;

import java.sql.PreparedStatement;

import Config.Connexion;
import com.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class UserRepository {
    private final Connexion connection;

    public UserRepository() {
        this.connection = Connexion.getInstance();
    }

    public List<User> userList() {
        List<User> userList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM users ORDER BY id";
            Statement s = connection.connectToDB().createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                User user = new User(id, name, age);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users"; // Votre requête SQL

        try (PreparedStatement stmt = connection.connectToDB().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                User user = new User(id, name, age);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception comme vous le souhaitez
        }
        return users;
    }

    public User addUser(String name, int age) {
        User newUser = null;
        try {
            String sql = "INSERT INTO users (name, age, consommation_totale) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setInt(3, 0);
            ps.executeUpdate();
            newUser = new User(name, age);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    public boolean foundUser(int id) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUSer(int id) {
        if (!foundUser(id)) {
            System.out.println("Utilisateur non trouvé.");
        }
        try {
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User editUser(int id, String newName, int age) {
        if (!foundUser(id)) {
            System.out.println("Utilisateur non trouvé.");
        }
        try {
            String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ps.setString(1, newName);
            ps.setInt(2, age);
            ps.setInt(3, id);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                return new User(id, newName, age);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the full exception stack trace
        }
        return null;
    }

    public Optional<User> getUserById(int id) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                float consommation = rs.getFloat("consommation_totale");

                return Optional.of(new User(id, name, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
