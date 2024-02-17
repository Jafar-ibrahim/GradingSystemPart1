package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorSectionDAO {

    private final DataSource dataSource;

    public InstructorSectionDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertInstructorSection(int instructorId, int sectionId) throws SQLException {
        String sql = "INSERT INTO instructor_section(instructor_id, section_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, instructorId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteInstructorSection(int instructorId, int sectionId) throws SQLException {
        String sql = "DELETE FROM instructor_section WHERE instructor_id = ? AND section_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, instructorId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.executeUpdate();
        }
    }

    public boolean InstructorSectionExists(int instructorId, int sectionId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM instructor_section WHERE instructor_id = ? AND section_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, instructorId);
            preparedStatement.setInt(2, sectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

}
