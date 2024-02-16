package Service;

import DAO.CourseDAO;

import javax.sql.DataSource;
import java.sql.SQLException;

public class CourseService {

    private final DataSource dataSource;
    private final CourseDAO courseDAO;

    public CourseService(DataSource dataSource) {
        this.dataSource = dataSource;
        courseDAO = new CourseDAO(dataSource);
    }

    public void addCourse(String courseName){
        try{
            courseDAO.insertCourse(courseName);
        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }
    public void deleteCourse(int courseId){
        try{
            courseDAO.deleteCourse(courseId);
        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }
}
