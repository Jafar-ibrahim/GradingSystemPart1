package DAO;

import javax.sql.DataSource;
import java.sql.*;
import Exception.*;

public class UserDAO {

    private final DataSource dataSource;

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public int insertUser(String username, String password,
                                  String firstName, String lastName, int roleId) throws SQLException {
        int userId = -1;
        String sql = "INSERT INTO user(username, password, first_name, last_name, role_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setInt(5, roleId);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("User creation failed, no ID obtained.");
                }
            }
        }

        return userId;
    }
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM user WHERE user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    public static void checkUserExists(Connection connection, int userId) throws SQLException {
        String sql = "SELECT 1 FROM user WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(!resultSet.next())
                    throw new UserNotFoundException();
            }
        }
    }
    public String getFullName(int userId) throws SQLException {
        String sql = "SELECT first_name, last_name FROM user WHERE user_id = ?";
        String fullName = null; // Default value if user not found

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    fullName = firstName + " " + lastName;
                }
            }

        }

        return fullName;
    }
    public int getRole(int userId) throws SQLException {
        String sql = "SELECT role_id FROM user WHERE user_id = ?";
        int roleId = -1; // Default value if user not found or role not set

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    roleId = resultSet.getInt("role_id");
                }
            }

        }
        return roleId;
    }
    public int authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT user_id FROM user WHERE username = ? AND password = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User authenticated, return user_id
                    return resultSet.getInt("user_id");
                } else {
                    // User not authenticated
                    return -1;
                }
            }

        }
    }
}
