package DAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getInstructorCourses(int instructorId) throws SQLException {
        String sql = "SELECT c.course_name, s.section_id " +
                "FROM instructor_section isec " +
                "JOIN section s ON isec.section_id = s.section_id " +
                "JOIN course c ON s.course_id = c.course_id " +
                "WHERE isec.instructor_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, instructorId);

            List<Map<String, Object>> courses = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("course_name", resultSet.getString("course_name"));
                courseMap.put("section_id", resultSet.getInt("section_id"));
                courses.add(courseMap);
            }

            resultSet.close();
            return courses;
        }
    }

}
