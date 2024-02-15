package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeDAO {

    private final DataSource dataSource;

    public GradeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertGrade(Connection connection, int studentId, int grade, int sectionId) throws SQLException {
        String sql = "INSERT INTO grade(student_id, section_id, grade) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.setInt(3, grade);
            preparedStatement.executeUpdate();
        }
    }
    public int updateGrade(int studentId, int sectionId, int newGrade) throws SQLException {
        String sql = "UPDATE grade SET grade = ? WHERE student_id = ? AND section_id = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, newGrade);
            preparedStatement.setInt(2, studentId);
            preparedStatement.setInt(3, sectionId);
            return preparedStatement.executeUpdate();
        }
    }
    public void deleteGrade(int studentId, int sectionId) throws SQLException {
        String sql = "DELETE FROM grade WHERE student_id = ? AND section_id = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.executeUpdate();
        }
    }
    public ResultSet getStudentGrades(int studentId) throws SQLException {
        String sql = "WITH SectionAverage AS ( " +
                "    SELECT ss.section_id, AVG(g.grade) AS average_grade " +
                "    FROM student_section ss " +
                "    JOIN grade g ON ss.section_id = g.section_id " +
                "    WHERE ss.student_id = ? " +
                "    GROUP BY ss.section_id " +
                ") " +
                "SELECT c.course_name, s.section_id, g.grade, sa.average_grade " +
                "FROM grade g " +
                "JOIN section s ON g.section_id = s.section_id " +
                "JOIN course c ON s.course_id = c.course_id " +
                "JOIN SectionAverage sa ON s.section_id = sa.section_id " +
                "WHERE g.student_id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, studentId);

            return preparedStatement.executeQuery();
        }
    }


    public ResultSet getStudentAverage( int studentId) throws SQLException {
        String sql = "SELECT AVG(grade) AS overall_average " +
                "FROM grades " +
                "WHERE student_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);

            return preparedStatement.executeQuery();
        }
    }
}
