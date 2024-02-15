package DAO;

import javax.sql.DataSource;
import java.sql.*;

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



}
