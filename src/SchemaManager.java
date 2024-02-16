import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaManager {
    private final DataSource dataSource;
    private PreparedStatement statement;

    public SchemaManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void initializeTables(){
        try{
            createRoleTable();
            createUserTable();
            createAdminTable();
            createInstructorTable();
            createStudentTable();
            createCourseTable();
            createSectionTable();
            createGradeTable();
            createStudent_SectionTable();
            createInstructor_SectionTable();
        }catch (SQLException sqlException){
            System.out.println("Tables initialization failed...");
            System.out.println(sqlException.getMessage());
        }
    }
    public void dropAllTablesIfExist(){
        String[] tablesNames = new String[]{"instructor_section","student_section","grade","section","course","student","instructor","admin","user","role"};
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){

            for(String table : tablesNames){
                statement.execute("DROP TABLE IF EXISTS "+table);
            }
        }catch (SQLException sqlException){
            System.out.println("Failed to drop tables");
            System.out.println(sqlException.getMessage());
        }
    }
    private void createRoleTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE role (" +
                    "role_id INT PRIMARY KEY," +
                    "name VARCHAR(255) UNIQUE NOT NULL)");
        }
    }
    private void createUserTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE user (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password_hash VARCHAR(255) NOT NULL," +
                    "first_name VARCHAR(255)," +
                    "last_name VARCHAR(255)," +
                    "role_id INT," +
                    "FOREIGN KEY (role_id) REFERENCES role(role_id))");
        }
    }
    private void createStudentTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS student (" +
                    "student_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES user(user_id))");
        }
    }

    private void createInstructorTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS instructor (" +
                    "instructor_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES user(user_id))");
        }
    }

    private void createAdminTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS admin (" +
                    "admin_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES user(user_id))");
        }
    }
    private void createCourseTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE course (" +
                    "course_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "course_name VARCHAR(255) NOT NULL)");
        }
    }
    private void createGradeTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE grade (" +
                    "student_id INT," +
                    "section_id INT," +
                    "grade INT," +
                    "PRIMARY KEY (student_id, section_id)," +
                    "FOREIGN KEY (student_id) REFERENCES student(student_id)," +
                    "FOREIGN KEY (section_id) REFERENCES section(section_id))");
        }
    }
    private void createSectionTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE section (" +
                    "section_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "course_id INT," +
                    "course_name VARCHAR(255)," +
                    "FOREIGN KEY (course_id) REFERENCES course(course_id))");
        }
    }
    private void createStudent_SectionTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE student_section (" +
                    "student_id INT," +
                    "section_id INT," +
                    "PRIMARY KEY (student_id, section_id)," +
                    "FOREIGN KEY (student_id) REFERENCES user(user_id)," +
                    "FOREIGN KEY (section_id) REFERENCES section(section_id))");
        }
    }
    private void createInstructor_SectionTable() throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE instructor_section (" +
                    "instructor_id INT," +
                    "section_id INT," +
                    "PRIMARY KEY (instructor_id, section_id)," +
                    "FOREIGN KEY (instructor_id) REFERENCES user(user_id)," +
                    "FOREIGN KEY (section_id) REFERENCES section(section_id))");
        }
    }

}
