package DAO;

import javax.sql.DataSource;
import java.sql.*;
import Exception.*;

public class CourseDAO {

    private final DataSource dataSource;

    public CourseDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertCourse(String courseName) throws SQLException {
        String sql = "INSERT INTO course(course_name) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.executeUpdate();
        }
    }
    public void deleteCourse(int courseId) throws SQLException {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.executeUpdate();
        }
    }

    public static void checkCourseExists(Connection connection, int courseId) throws SQLException {
        String sql = "SELECT 1 FROM course WHERE course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(!resultSet.next())
                    throw new CourseNotFoundException();
            }
        }
    }

    public ResultSet getInstructorCourses(int instructorId) throws SQLException {
        String sql = "SELECT c.course_name, s.section_id " +
                "FROM instructor_section isec " +
                "JOIN section s ON isec.section_id = s.section_id " +
                "JOIN course c ON s.course_id = c.course_id " +
                "WHERE isec.instructor_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, instructorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet;
            }
        }
    }

}
