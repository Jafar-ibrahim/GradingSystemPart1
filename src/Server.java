import Service.*;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Random;

public class Server  {

    private  DataSource dataSource;
    private  SchemaManager schemaManager;
    private  EnrollmentService enrollmentService;
    private  GradeService gradeService;
    private  UserService userService;
    private RoleService roleService;
    private CourseService courseService;
    private SectionService sectionService;

    public Server() {
        try {
            dataSource = getDataSource();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        schemaManager = new SchemaManager(dataSource);
        roleService = new RoleService(dataSource);
        enrollmentService = new EnrollmentService(dataSource);
        gradeService = new GradeService(dataSource);
        userService = new UserService(dataSource);
    }

    public static void main(String[] args) throws SQLException {
        Server server = new Server();

        server.schemaManager.dropAllTablesIfExist();
        server.schemaManager.initializeTables();
        server.insertDummyData();
        /*insertUser(dataSource.getConnection(), 11,"test","sdfsf","sdfsdf","sdfsdf",3);
        insertStudentSection(dataSource.getConnection(), 11,1);
        insertGrade(dataSource.getConnection(),11,85,1);*/
        //insertStudentSection(dataSource.getConnection(), 5,5);
        //insertStudentSection(dataSource.getConnection(), 1,6);
        //insertGrade(dataSource.getConnection(),5,80,5);
        //insertGrade(dataSource.getConnection(),4,65,5);
        //viewCombinedInformation(dataSource.getConnection(), 1);
        //assignInstructorToSection(getDataSource().getConnection(),1,2);
        //deassignInstructorFromSection(getDataSource().getConnection(), 1,2);
        /*insertGrade(dataSource.getConnection(),3,70,4);
        addStudent(dataSource.getConnection(), "test","sdfsf","sdfsdf","sdfsdf");
        insertStudentSection(dataSource.getConnection(), 5,4);
        insertGrade(dataSource.getConnection(),5,40,4);*/
        //viewCombinedInformation(dataSource.getConnection(), 5);

        /*deassignInstructorFromSection(getDataSource().getConnection(), 8,6);
        assignInstructorToSection(dataSource.getConnection(),1,1);
        assignInstructorToSection(dataSource.getConnection(),2,2);
        assignInstructorToSection(dataSource.getConnection(),2,3);*/

        //System.out.println(isInstructorAssigned(getDataSource().getConnection(),2 ,3));
        //updateGrade(getDataSource().getConnection(), 5,1,100);
        //printInstructorCourses(getDataSource().getConnection(), 2);

    }



    public static DataSource getDataSource() throws SQLException {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("grading_system");
        ds.setUser("root");
        ds.setPassword("qwerty");
        ds.setUseSSL(false);
        ds.setAllowPublicKeyRetrieval(true);

        return ds;
    }



    private void insertDummyData()  {
        try (Connection connection = dataSource.getConnection()) {
            // Insert dummy roles
            roleService.addRole(1, "admin");
            roleService.addRole(2, "instructor");
            roleService.addRole(3, "student");

            String[] names = new String[]{"Jafar","Omar","Ahmad","Fatima","Sara","sffs","sdfsdf","saif","yousef"};
            // Insert dummy users
            userService.addAdmin("Admin1","password" , "FirstName", "LastName");
            userService.addInstructor("Instructor1","password" , "FirstName", "LastName");
            userService.addInstructor("Instructor2","password" , "FirstName", "LastName");
            userService.addInstructor("Instructor3","password" , "FirstName", "LastName");

            for (int i = 1; i <= names.length; i++) {
                userService.addStudent(names[i-1], "password" + i, "FirstName" + i, "LastName" + i);
            }

            String[] courses = new String[]{"Data Structures","Parallel Processing","Java Programming","Operating Systems","Networks"};
            // Insert dummy courses
            for (int i = 1; i <= 5; i++) {
                c.insertCourse(courses[i-1]);
            }

            sectionService.addSection(1 );
            sectionService.addSection(  1 );
            sectionService.addSection(2 );
            sectionService.addSection(3 );
            sectionService.addSection(4 );
            sectionService.addSection(5 );

            insertStudentSection(connection, 1, 1);
            insertStudentSection(connection, 2, 1);
            insertStudentSection(connection, 3, 4);
            insertStudentSection(connection, 4, 5);

            assignInstructorToSection(connection,1,1);
            assignInstructorToSection(connection,2,2);
            assignInstructorToSection(connection,2,3);

            Random random = new Random();


            insertGrade(connection, 1, random.nextInt(101), 1);
            insertGrade(connection, 2, random.nextInt(101), 1);



            System.out.println("Dummy data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







}
