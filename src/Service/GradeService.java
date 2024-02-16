package Service;

import DAO.GradeDAO;
import DAO.SectionDAO;
import DAO.StudentDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeService {

    private final DataSource dataSource;
    private final GradeDAO gradeDAO;

    public GradeService(DataSource dataSource) {
        this.dataSource = dataSource;
        gradeDAO = new GradeDAO(dataSource);
    }

    public void addGrade(int studentId, int grade, int sectionId){
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            gradeDAO.insertGrade(studentId,grade,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteGrade(int studentId, int sectionId){
        int affectedRows = 0;
        try {
            affectedRows = gradeDAO.deleteGrade(studentId,sectionId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (affectedRows > 0) {
            System.out.println("Grade deleted successfully.");
        } else {
            System.out.println("No matching record found for the specified student and section.");
        }
    }

    public void printGradeReport(int studentId) throws SQLException {
        ResultSet resultSet = gradeDAO.getStudentGrades(studentId);
        // Print table header
        System.out.printf("%-20s%-15s%-15s%-15s%n", "Course Name", "Section ID", "Grade", "Section Average");
        System.out.println("--------------------------------------------------");

        // Print result set
        while (resultSet.next()) {
            String courseName = resultSet.getString("course_name");
            int sectionId = resultSet.getInt("section_id");
            double grade = resultSet.getDouble("grade");
            double sectionAverage = resultSet.getDouble("average_grade");

            // Print each row
            System.out.printf("%-20s%-15s%-15s%-15s%n", courseName, sectionId, grade, sectionAverage);
        }
    }

    public void printStudentAverage(int studentId) throws SQLException {
        ResultSet resultSet = gradeDAO.getStudentAverage(studentId);

        if(resultSet.next()) {
            double overallAvg = resultSet.getDouble("overall_average");
            System.out.println("Overall Average Across All Courses: " + overallAvg + "\n");
        }
    }
    public void printCombinedInformation(int studentId) throws SQLException {
        System.out.println("Combined Information for Student " + studentId + ":\n");

        // View grades in all courses
        printGradeReport(studentId);

        // View average across all courses
        printStudentAverage(studentId);

    }

    public void updateGrade(int studentId, int sectionId, int newGrade){
        int affectedRows = 0;
        try {
            affectedRows = gradeDAO.updateGrade(studentId,sectionId,newGrade);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (affectedRows > 0) {
            System.out.println("Grade updated successfully.");
        } else {
            System.out.println("No matching record found for the specified student and section.");
        }
    }
}
