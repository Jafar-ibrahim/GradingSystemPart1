package Service;

import DAO.CourseDAO;
import DAO.SectionDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class SectionService {
    private final DataSource dataSource;
    private final SectionDAO sectionDAO;

    public SectionService(DataSource dataSource) {
        this.dataSource = dataSource;
        sectionDAO = new SectionDAO(dataSource);
    }

    public String addSection(int courseId){
        try(Connection connection = dataSource.getConnection()){
            CourseDAO.checkCourseExists(connection,courseId);
            sectionDAO.insertSection(courseId);
            return "Section added successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Section addition failed";}
    }

    public String deleteSection(int sectionId){
        try(Connection connection = dataSource.getConnection()){
            SectionDAO.checkSectionExists(connection,sectionId);
            sectionDAO.deleteSection(sectionId);
            return "Section deleted successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Section deletion failed";
        }
    }

    public String getSectionCourseName(int courseId){
        try(Connection connection = dataSource.getConnection()){
            CourseDAO.checkCourseExists(connection,courseId);
            return sectionDAO.getCourseName(courseId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
