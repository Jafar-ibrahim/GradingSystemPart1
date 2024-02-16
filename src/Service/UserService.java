package Service;

import DAO.AdminDAO;
import DAO.InstructorDAO;
import DAO.StudentDAO;
import DAO.UserDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO;
    private final AdminDAO adminDAO;
    private final InstructorDAO instructorDAO;
    private final StudentDAO studentDAO;

    public UserService(DataSource dataSource) {
        this.userDAO = new UserDAO(dataSource);
        this.adminDAO = new AdminDAO(dataSource);
        this.instructorDAO = new InstructorDAO(dataSource);
        this.studentDAO = new StudentDAO(dataSource);
    }

    public void addAdmin(String username, String password, String firstName, String lastName) throws SQLException {
        // Create a user and retrieve the generated user_id
        int userId = userDAO.insertUser(username, password, firstName, lastName,1);

        // Insert an admin with the generated user_id
        adminDAO.insertAdmin(userId);
    }
    public void addStudent( String username, String password,
                                   String firstName, String lastName) throws SQLException {
        int userID = userDAO.insertUser(username,password,firstName,lastName,3);
        studentDAO.insertStudent(userID);

    }

    public void addInstructor( String username, String password, String firstName, String lastName) throws SQLException {
        // Create a user and retrieve the generated user_id
        int userId = userDAO.insertUser(username, password, firstName, lastName,2);

        // Insert an instructor with the generated user_id
        instructorDAO.insertInstructor(userId);
    }
    public void deleteAdmin(int adminId) throws SQLException {
        // Delete admin
        adminDAO.deleteAdmin(adminId);

        // Delete associated user
        userDAO.deleteUser(adminId);
    }
    public void deleteInstructor(int instructorId) throws SQLException {
        // Delete instructor
        instructorDAO.deleteInstructor(instructorId);

        // Delete associated user
        userDAO.deleteUser(instructorId);
    }
    public void deleteStudent(int studentId) throws SQLException {
        // Delete student
        studentDAO.deleteStudent( studentId);

        // Delete associated user
        userDAO.deleteUser(studentId);
    }

}
