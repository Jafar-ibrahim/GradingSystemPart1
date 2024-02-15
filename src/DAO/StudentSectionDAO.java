package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentSectionDAO {

    private final DataSource dataSource;

    public StudentSectionDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertStudentSection(Connection connection, int studentId, int sectionId) throws SQLException {
        String sql = "INSERT INTO student_section(student_id, section_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteStudentSection(int studentId, int sectionId) throws SQLException {
        String sql = "DELETE FROM student_section WHERE student_id = ? AND section_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.executeUpdate();
        }
    }

}
