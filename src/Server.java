import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Random;

public class Server  {

    private static DataSource dataSource;
    private static SchemaManager schemaManager;

    public Server() {
    }

    public static void main(String[] args) throws SQLException {
        try {
            dataSource = getDataSource();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        schemaManager = SchemaManager.getInstance(dataSource);

        schemaManager.dropAllTablesIfExist();
        schemaManager.initializeTables();
        insertDummyData();
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



    private static void insertDummyData()  {
        try (Connection connection = dataSource.getConnection()) {
            // Insert dummy roles
            insertRole(connection, 1, "admin");
            insertRole(connection, 2, "instructor");
            insertRole(connection, 3, "student");

            String[] names = new String[]{"Jafar","Omar","Ahmad","Fatima","Sara","sffs","sdfsdf","saif","yousef"};
            // Insert dummy users
            addAdmin(connection,"Admin1","password" , "FirstName", "LastName");
            addInstructor(connection,"Instructor1","password" , "FirstName", "LastName");
            addInstructor(connection,"Instructor2","password" , "FirstName", "LastName");
            addInstructor(connection,"Instructor3","password" , "FirstName", "LastName");

            for (int i = 1; i <= names.length; i++) {
                addStudent(connection, names[i-1], "password" + i, "FirstName" + i, "LastName" + i);
            }

            String[] courses = new String[]{"Data Structures","Parallel Processing","Java Programming","Operating Systems","Networks"};
            // Insert dummy courses
            for (int i = 1; i <= 5; i++) {
                insertCourse(connection, courses[i-1]);
            }

            insertSection(connection,  1 );
            insertSection(connection,  1 );
            insertSection(connection,  2 );
            insertSection(connection,  3 );
            insertSection(connection,  4 );
            insertSection(connection,  5 );

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
    public static void addAdmin(Connection connection, String username, String password, String firstName, String lastName) throws SQLException {
        // Create a user and retrieve the generated user_id
        int userId = insertUser(connection, username, password, firstName, lastName,1);

        // Insert an admin with the generated user_id
        insertAdmin(connection, userId);
    }
    private static void addStudent(Connection connection, String username, String password,
                            String firstName, String lastName) throws SQLException {
        int userID = insertUser(connection,username,password,firstName,lastName,3);
        insertStudent(connection,userID);

    }

    public static void addInstructor(Connection connection, String username, String password, String firstName, String lastName) throws SQLException {
        // Create a user and retrieve the generated user_id
        int userId = insertUser(connection, username, password, firstName, lastName,2);

        // Insert an instructor with the generated user_id
        insertInstructor(connection, userId);
    }








    // View combined information: grades, section averages, and overall average
    private static void viewCombinedInformation(Connection connection, int studentId) throws SQLException {
        System.out.println("Combined Information for Student " + studentId + ":\n");

        // View grades in all courses
        viewStudentGrades(connection, studentId);

        // View average across all courses
        viewStudentAverage(connection, studentId);

    }





}
