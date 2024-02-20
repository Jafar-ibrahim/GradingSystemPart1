import Service.*;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Random;

public class Server  {

    private int port;
    private  DataSource dataSource;
    private  SchemaManager schemaManager;
    private  EnrollmentService enrollmentService;
    private  GradeService gradeService;
    private  UserService userService;
    private RoleService roleService;
    private CourseService courseService;
    private SectionService sectionService;

    public Server(int port) {
        this.port = port;
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
        courseService = new CourseService(dataSource);
        sectionService = new SectionService(dataSource);
    }

    public static void main(String[] args) throws SQLException, IOException {
<<<<<<< Updated upstream
        int port = 9090;
=======
        int port = 7070;
>>>>>>> Stashed changes
        Server server = new Server(port);
        server.schemaManager.dropAllTablesIfExist();
        server.schemaManager.initializeTables();
        server.addDummyData();

        server.start();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started at port: "+port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new thread for handling the client
                new Thread(new ClientHandler(clientSocket,dataSource)).start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }




    public static DataSource getDataSource() throws SQLException {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("grading_system");
        ds.setUser("root");
        ds.setPassword("1234");
        ds.setUseSSL(false);
        ds.setAllowPublicKeyRetrieval(true);

        return ds;
    }



    private void addDummyData()  {
        try (Connection connection = dataSource.getConnection()) {
            roleService.addRole(1, "admin");
            roleService.addRole(2, "instructor");
            roleService.addRole(3, "student");

            String[] names = new String[]{"Jafar","Omar","Ahmad","Fatima","Sara","sffs","sdfsdf","saif","yousef"};
            userService.addAdmin("Admin1","password" , "FirstName", "LastName");
            userService.addInstructor("Instructor1","password" , "FirstName", "LastName");
            userService.addInstructor("Instructor2","password" , "FirstName", "LastName");
            userService.addInstructor("Instructor3","password" , "FirstName", "LastName");

            for (int i = 1; i <= names.length; i++) {
                userService.addStudent(names[i-1], "password" + i, "FirstName" + i, "LastName" + i);
            }

            String[] courses = new String[]{"Data Structures","Parallel Processing","Java Programming","Operating Systems","Networks"};
            for (int i = 1; i <= 5; i++) {
                courseService.addCourse(courses[i-1]);
            }

            sectionService.addSection(1 );
            sectionService.addSection( 1 );
            sectionService.addSection(2 );
            sectionService.addSection(3 );
            sectionService.addSection(4 );
            sectionService.addSection(5 );

            enrollmentService.addStudentToSection(1, 1);
            enrollmentService.addStudentToSection(2, 1);
            enrollmentService.addStudentToSection(3, 2);
            enrollmentService.addStudentToSection(4, 2);
            enrollmentService.addStudentToSection(1, 3);
            enrollmentService.addStudentToSection(2, 3);
            enrollmentService.addStudentToSection(3, 3);
            enrollmentService.addStudentToSection(8, 4);
            enrollmentService.addStudentToSection(9, 4);

            enrollmentService.addInstructorToSection(1,1);
            enrollmentService.addInstructorToSection(2,2);
            enrollmentService.addInstructorToSection(2,3);
            enrollmentService.addInstructorToSection(3,4);

            Random random = new Random();
            gradeService.addGrade(1,random.nextInt(101), 1);
            gradeService.addGrade(2,random.nextInt(101), 1);
            gradeService.addGrade(3,random.nextInt(101), 2);
            gradeService.addGrade(4,random.nextInt(101), 2);
            gradeService.addGrade(1,random.nextInt(101), 3);
            gradeService.addGrade(2,random.nextInt(101), 3);
            gradeService.addGrade(3,random.nextInt(101), 3);
            gradeService.addGrade(8,random.nextInt(101), 4);
            gradeService.addGrade(9,random.nextInt(101), 4);



            System.out.println("Dummy data added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
