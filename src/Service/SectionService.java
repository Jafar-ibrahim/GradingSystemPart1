package Service;

import DAO.CourseDAO;
import DAO.SectionDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SectionService {
    private final DataSource dataSource;
    private final SectionDAO sectionDAO;

    public SectionService(DataSource dataSource) {
        this.dataSource = dataSource;
        sectionDAO = new SectionDAO(dataSource);
    }

    public void addSection(int courseId){
        try(Connection connection = dataSource.getConnection()){
            CourseDAO.checkCourseExists(connection,courseId);
            sectionDAO.insertSection(courseId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteSection(int courseId){
        try(Connection connection = dataSource.getConnection()){
            CourseDAO.checkCourseExists(connection,courseId);
            sectionDAO.deleteSection(courseId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public String getCourseName(int courseId){
        try(Connection connection = dataSource.getConnection()){
            CourseDAO.checkCourseExists(connection,courseId);
            return sectionDAO.getCourseName(courseId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
