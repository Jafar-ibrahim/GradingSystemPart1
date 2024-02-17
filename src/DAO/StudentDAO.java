package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    private final DataSource dataSource;

    public StudentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void insertStudent(int userId) throws SQLException {
        String sql = "INSERT INTO student(user_id) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM student WHERE student_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean checkStudentExists(Connection connection, int studentId) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public int getStudentId(int userId) throws SQLException {
        String sql = "SELECT student_id FROM student WHERE user_id = ?";
        int studentId = -1; // Default value if student not found

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    studentId = resultSet.getInt("student_id");
                }
            }

        }
        return studentId;
    }

    public String getStudentFullName( int studentId) throws SQLException {
        String sql = "SELECT CONCAT(u.first_name, ' ', u.last_name) AS full_name " +
                "FROM user u " +
                "JOIN student i ON u.user_id = i.user_id " +
                "WHERE i.student_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("full_name");
                }
            }

        }
        return null;
    }


}
