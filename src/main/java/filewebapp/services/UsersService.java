package filewebapp.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;

public class UsersService {
    private static final String url = "jdbc:mysql://localhost:3306/filelisting_db?user=filewebapp&password=files";

    public boolean register(String login, String email, String password) {
        String hash = Arrays.toString(getPasswordHash(password));

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            int rowsInserted = statement.executeUpdate(
                    "INSERT INTO users(login, email, password_hash, home_catalog) VALUES (\""
                            + login + "\", \"" + email + "\", \"" + hash + "\", \"" + login + "\");");
            return rowsInserted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String login, String password) {
        String hash = Arrays.toString(getPasswordHash(password));

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT password_hash FROM users WHERE login = \"" + login + "\";");
        ) {
            return resultSet.next() && resultSet.getString("password_hash").equals(hash);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginRegistered(String login) {
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM users WHERE login = \"" + login + "\";");
        ) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailRegistered(String email) {
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM users WHERE email = \"" + email + "\";");
        ) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private byte[] getPasswordHash(String password) {
        byte[] hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[1], 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hash == null)
            return new byte[0];
        else
            return hash;
    }
}