package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeDAO {

    private final DataSource dataSource;

    public GradeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertGrade(int studentId, double grade, int sectionId) throws SQLException {
        String sql = "INSERT INTO grade(student_id, section_id, grade) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, sectionId);
            preparedStatement.setDouble(3, grade);
            return preparedStatement.executeUpdate();
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
    public int deleteGrade(int studentId, int sectionId) throws SQLException {
        String sql = "DELETE FROM grade WHERE student_id = ? AND section_id = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, sectionId);
            return preparedStatement.executeUpdate();
        }
    }
    public List<Map<String, Object>> getStudentGrades(int studentId) throws SQLException {
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

            List<Map<String, Object>> grades = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> gradeMap = new HashMap<>();
                gradeMap.put("course_name", resultSet.getString("course_name"));
                gradeMap.put("section_id", resultSet.getInt("section_id"));
                gradeMap.put("grade", resultSet.getDouble("grade"));
                gradeMap.put("average_grade", resultSet.getDouble("average_grade"));
                grades.add(gradeMap);
            }

            resultSet.close();
            return grades;
        }
    }


    public double getStudentAverage( int studentId) throws SQLException {
        String sql = "SELECT AVG(grade) AS overall_average " +
                "FROM grade " +
                "WHERE student_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getDouble("overall_average");
            }
        }
        return 0.0;
    }

    public List<Map<String, Object>> getGradesBySectionId(int sectionId) throws SQLException {
        String sql = "SELECT g.student_id, g.grade " +
                "FROM grade g " +
                "WHERE g.section_id = ?";

        List<Map<String, Object>> gradesList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, sectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> gradesMap = new HashMap<>();

                    int studentId = resultSet.getInt("student_id");
                    double grade = resultSet.getDouble("grade");

                    gradesMap.put("section_id", sectionId);
                    gradesMap.put("student_id", studentId);
                    gradesMap.put("grade", grade);

                    double averageGrade = calculateSectionAverage(sectionId);
                    gradesMap.put("average_grade", averageGrade);

                    gradesList.add(gradesMap);
                }
            }
        }

        return gradesList;
    }
    private double calculateSectionAverage(int sectionId) throws SQLException {
        String sql = "SELECT AVG(grade) AS average_grade " +
                "FROM grade " +
                "WHERE section_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, sectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("average_grade");
                }
            }
        }
        // Default value if there is an issue or no data is found
        return 0.0;
    }
}
