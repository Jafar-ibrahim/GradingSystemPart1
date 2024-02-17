package Service;

import DAO.GradeDAO;
import DAO.SectionDAO;
import DAO.StudentDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GradeService {

    private final DataSource dataSource;
    private final GradeDAO gradeDAO;
    private final StudentDAO studentDAO;

    public GradeService(DataSource dataSource) {
        this.dataSource = dataSource;
        gradeDAO = new GradeDAO(dataSource);
        studentDAO = new StudentDAO(dataSource);
    }

    public String addGrade(int studentId, int grade, int sectionId){
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            gradeDAO.insertGrade(studentId,grade,sectionId);
            return "Grade added successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Grade addition failed.";
        }
    }

    public String deleteGrade(int studentId, int sectionId){
        int affectedRows = 0;
        try{
            affectedRows = gradeDAO.deleteGrade(studentId,sectionId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (affectedRows > 0) {
            return "Grade deleted successfully.";
        } else {
            return "No matching record found for the specified student and section.";
        }
    }
    public String updateGrade(int studentId, int sectionId, int newGrade){
        int affectedRows = 0;
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            affectedRows = gradeDAO.updateGrade(studentId,sectionId,newGrade);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (affectedRows > 0) {
            return "Grade updated successfully.";
        } else {
            return "No matching record found for the specified student and section.";
        }
    }

    public String getGradeReport(int studentId){
        List<Map<String, Object>> grades = null;
        try {
            grades = gradeDAO.getStudentGrades(studentId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append(String.format("%-20s%-15s%-15s%-15s\n", "Course Name", "Section ID", "Grade", "Section Average"));
        reportBuilder.append("---------------------------------------------------------------------\n");

        assert grades != null;
        for (Map<String, Object> grade : grades) {
            String courseName = (String) grade.get("course_name");
            int sectionId = (Integer) grade.get("section_id");
            double gradeValue = (Double) grade.get("grade");
            double sectionAverage = (Double) grade.get("average_grade");

            reportBuilder.append(String.format("%-20s%-15s%-15s%-15s\n", courseName, sectionId, gradeValue, sectionAverage));
        }

        return reportBuilder.toString();
    }

    public String getStudentAverage(int studentId) {
        double overallAvg = 0;
        try {
            overallAvg = gradeDAO.getStudentAverage(studentId);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return "Overall Average Across All Courses: " + overallAvg + "\n";
    }
    public String getCombinedInformation(int studentId){
        System.out.println("Combined Information for Student " + studentId + ":\n");

        return getGradeReport(studentId)+"\n"+ getStudentAverage(studentId);

    }



    public String getSectionGrades(int sectionId) {
        List<Map<String, Object>> gradesList;
        try {
            gradesList = gradeDAO.getGradesBySectionId(sectionId);
            StringBuilder outputBuilder = new StringBuilder();

            outputBuilder.append("Section ").append(sectionId).append(" grades:\n");
            outputBuilder.append("-----------------------------------------------\n");

            outputBuilder.append(String.format("%-15s%-25s%-15s\n", "Student ID", "Student_Full_Name", "Grade"));
            outputBuilder.append("-----------------------------------------------\n");

            for (Map<String, Object> gradesMap : gradesList) {
                int studentId = (int) gradesMap.get("student_id");
                String fullName = studentDAO.getStudentFullName(studentId);
                double grade = (double) gradesMap.get("grade");
                outputBuilder.append(String.format("%-15s%-25s%-15s\n", studentId, fullName, grade));
            }

            if (!gradesList.isEmpty()) {
                double averageGrade = (double) gradesList.get(0).get("average_grade");
                outputBuilder.append("\nAverage Grade for Section: ").append(averageGrade).append("\n");
            }

            return outputBuilder.toString();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "";
        }


    }


}
