package Service;

import DAO.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void addStudentToSection(int studentId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            studentSectionDAO.insertStudentSection(studentId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void removeStudentFromSection(int studentId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            StudentDAO.checkStudentExists(connection,studentId);
            SectionDAO.checkSectionExists(connection,sectionId);
            studentSectionDAO.deleteStudentSection(studentId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void addInstructorToSection(int InstructorId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            InstructorDAO.checkInstructorExists(connection,InstructorId);
            SectionDAO.checkSectionExists(connection,sectionId);
            instructorSectionDAO.insertInstructorSection(InstructorId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void removeInstructorFromSection(int InstructorId , int sectionId){
        try(Connection connection = dataSource.getConnection()){
            InstructorDAO.checkInstructorExists(connection,InstructorId);
            SectionDAO.checkSectionExists(connection,sectionId);
            instructorSectionDAO.deleteInstructorSection(InstructorId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void printInstructorSections(int instructorId){

        try{
            ResultSet resultSet = courseDAO.getInstructorCourses(instructorId);

            String instructorName = instructorDAO.getInstructorFullName(instructorId);
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
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

}
