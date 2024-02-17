package Service;

import DAO.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EnrollmentService {
    private final DataSource dataSource;
    private final InstructorSectionDAO instructorSectionDAO;
    private final StudentSectionDAO studentSectionDAO;
    private final InstructorDAO instructorDAO;
    private final CourseDAO courseDAO;

    public EnrollmentService(DataSource dataSource) {
        this.dataSource = dataSource;
        instructorSectionDAO = new InstructorSectionDAO(dataSource);
        instructorDAO = new InstructorDAO(dataSource);
        studentSectionDAO = new StudentSectionDAO(dataSource);
        courseDAO = new CourseDAO(dataSource);
    }

    public String addStudentToSection(int studentId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            studentSectionDAO.insertStudentSection(studentId,sectionId);
            return "Student added to section successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Student addition to section failed";
        }
    }
    public String removeStudentFromSection(int studentId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            studentSectionDAO.deleteStudentSection(studentId,sectionId);
            return "Student removed from section successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Student removal from section failed";
        }
    }
    public String addInstructorToSection(int InstructorId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            InstructorDAO.checkInstructorExists(connection,InstructorId);
            SectionDAO.checkSectionExists(connection,sectionId);
            instructorSectionDAO.insertInstructorSection(InstructorId,sectionId);
            return "Instructor added to section successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Instructor addition to section failed";
        }
    }
    public String removeInstructorFromSection(int InstructorId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            InstructorDAO.checkInstructorExists(connection,InstructorId);
            SectionDAO.checkSectionExists(connection,sectionId);
            instructorSectionDAO.deleteInstructorSection(InstructorId,sectionId);
            return "Instructor removed from section successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Instructor removal from section failed";
        }
    }
    public boolean instructorHasAccessToSection(int instructorId, int sectionId){
        try{
            return instructorSectionDAO.InstructorSectionExists(instructorId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean studentIsInSection(int studentId, int sectionId){
        try{
            return studentSectionDAO.StudentSectionExists(studentId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public String getInstructorSections(int instructorId) {
        StringBuilder outputBuilder = new StringBuilder();

        try {
            List<Map<String, Object>> courses = courseDAO.getInstructorCourses(instructorId);

            String instructorName = instructorDAO.getInstructorFullName(instructorId);
            outputBuilder.append("Instructor (").append(instructorName).append(")'s sections:\n");

            outputBuilder.append(String.format("%-22s%-15s%n", "Section_id", "Course Name"));
            outputBuilder.append("----------------------------------------\n");

            for (Map<String, Object> course : courses) {
                String courseName = (String) course.get("course_name");
                int sectionNumber = (Integer) course.get("section_id");

                outputBuilder.append(String.format("%-20s%-25s%n", sectionNumber, courseName));
            }
        } catch (SQLException e) {
            outputBuilder.append(e.getMessage()).append("\n");
            e.printStackTrace();
        }

        return outputBuilder.toString();
    }


}
