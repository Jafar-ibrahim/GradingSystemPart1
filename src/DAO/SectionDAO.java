package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionDAO {

    private final DataSource dataSource;

    public SectionDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertSection(Connection connection, int courseId) throws SQLException {

        // Insert the new section
        String sql = "INSERT INTO section(course_id, course_name) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, getCourseName(connection, courseId));
            preparedStatement.executeUpdate();
        }
    }

    public String getCourseName(Connection connection, int courseId) throws SQLException {
        String sql = "SELECT course_name FROM course WHERE course_id = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("course_name");
                }
                throw new SQLException("Course not found for courseId: " + courseId);
            }
        }
    }
    public void deleteSection(int sectionId) throws SQLException {
        String sql = "DELETE FROM section WHERE section_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, sectionId);
            preparedStatement.executeUpdate();
        }
    }


}
