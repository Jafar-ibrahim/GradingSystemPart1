package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorDAO {

    private final DataSource dataSource;

    public InstructorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertInstructor( int userId) throws SQLException {
        String sql = "INSERT INTO instructor(user_id) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }
    public void printInstructorCourses(int instructorId) {
        String sql = "SELECT c.course_name, s.section_id " +
                "FROM instructor_section isec " +
                "JOIN section s ON isec.section_id = s.section_id " +
                "JOIN course c ON s.course_id = c.course_id " +
                "WHERE isec.instructor_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, instructorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                String instructorName = getInstructorFullName(instructorId);
                System.out.println("Instructor ("+instructorName+")'s sections :");
                // Print table header
                System.out.printf("%-22s%-15s%n","Section_no", "Course Name" );
                System.out.println("----------------------------------------");

                // Print result set
                while (resultSet.next()) {
                    String courseName = resultSet.getString("course_name");
                    int sectionNumber = resultSet.getInt("section_id");

                    // Print each row
                    System.out.printf("%-20s%-25s%n", sectionNumber, courseName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getInstructorFullName( int instructorId) {
        String sql = "SELECT CONCAT(u.first_name, ' ', u.last_name) AS full_name " +
                "FROM user u " +
                "JOIN instructor i ON u.user_id = i.user_id " +
                "WHERE i.instructor_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, instructorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("full_name");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isInstructorAssigned( int instructorId, int sectionId) throws SQLException {
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

    public void deleteInstructor(int instructorId) throws SQLException {
        String sql = "DELETE FROM instructor WHERE instructor_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, instructorId);
            preparedStatement.executeUpdate();
        }
    }


}
