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

    public String addCourse(String courseName){
        try{
            courseDAO.insertCourse(courseName);
            return "Course added successfully";
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Course addition failed";
        }
    }
    public String deleteCourse(int courseId){
        try{
            courseDAO.deleteCourse(courseId);
            return "Course deleted successfully";

        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Course deletion failed";   }
    }
}
