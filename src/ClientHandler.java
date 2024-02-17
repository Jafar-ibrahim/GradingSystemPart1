import Service.*;

import javax.sql.DataSource;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;
    private final UserService userService;
    private final CourseService courseService;
    private final SectionService sectionService;
    public ClientHandler(Socket clientSocket , DataSource dataSource) {
        this.clientSocket = clientSocket;
        enrollmentService = new EnrollmentService(dataSource);
        gradeService = new GradeService(dataSource);
        userService = new UserService(dataSource);
        courseService = new CourseService(dataSource);
        sectionService = new SectionService(dataSource);
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String message = inputStream.readUTF();
            String[] parts = message.split(",");
            String username = parts[0];
            String password = parts[1];

            int userId = userService.authenticateUser(username, password);
            if (userId == -1) {
                outputStream.writeUTF("[Authentication] Invalid credentials");
                outputStream.flush();
                return;
            }
            int userRole = userService.getUserRole(userId);
            int specificId = userService.getSpecificId(userId, userRole);
            String userFullName = userService.getUserFullName(userRole);
            outputStream.writeUTF(userId + "," + specificId + "," + userRole + "," + userFullName);
            outputStream.flush();


            outputStream.writeUTF("[Authentication] Successful");
            outputStream.flush();
            while(true) {
                message = inputStream.readUTF();
                parts = message.split(",");
                System.out.println("Received command : "+ Arrays.toString(parts));
                String result = dispatchCommand(parts,userRole);

                outputStream.writeUTF(result);
                outputStream.flush();
                if(parts[0].equals("Exit"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String dispatchCommand(String[] parts , int userRole){
        String command = parts[0];
        if (command.equals("Exit")) return "Exiting...";

        // Admin commands
        if(command.equals("add_course"))   return courseService.addCourse(parts[1]);
        if(command.equals("delete_course"))   return courseService.deleteCourse(Integer.parseInt(parts[1]));
        if(command.equals("add_section"))   return sectionService.addSection(Integer.parseInt(parts[1]));
        if(command.equals("delete_section"))   return sectionService.deleteSection(Integer.parseInt(parts[1]));
        if(command.equals("add_instructor"))   return userService.addInstructor(parts[1],parts[2],parts[3],parts[4]);
        if(command.equals("delete_instructor"))   return userService.deleteInstructor(Integer.parseInt(parts[1]));
        if(command.equals("add_student"))   return userService.addStudent(parts[1],parts[2],parts[3],parts[4]);
        if(command.equals("delete_student"))   return userService.deleteStudent(Integer.parseInt(parts[1]));
        if(command.equals("add_student_section"))   return enrollmentService.addStudentToSection(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
        if(command.equals("remove_student_section"))   return enrollmentService.removeStudentFromSection(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
        if(command.equals("add_instructor_section"))   return enrollmentService.addInstructorToSection(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
        if(command.equals("remove_instructor_section"))   return enrollmentService.removeInstructorFromSection(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));

        // Instructor commands
        if (userRole == 2) {
            int instructor_id = Integer.parseInt(parts[1]);
            if (command.equals("print_sections")) return enrollmentService.getInstructorSections(instructor_id);
            int section_id = Integer.parseInt(parts[2]);
            if(enrollmentService.instructorHasAccessToSection(instructor_id,section_id)) {
                if (command.equals("print_section_grades")) return gradeService.getSectionGrades(section_id);
                int student_id = Integer.parseInt(parts[3]);

                if (command.equals("delete_grade")) return gradeService.deleteGrade(student_id, section_id);
                int grade = Integer.parseInt(parts[4]);
                if(enrollmentService.studentIsInSection(student_id,section_id)) {
                    if (command.equals("add_grade")) return gradeService.addGrade(student_id, grade, section_id);
                    if (command.equals("update_grade")) return gradeService.updateGrade(student_id, section_id, grade);
                }
                else return "No student with the specified id exists in the section";
            }else
                return "Instructor has no access to the section ";
        }

        // Student commands
        if(command.equals("print_grades"))   return gradeService.getGradeReport(Integer.parseInt(parts[1]));
        if(command.equals("print_average"))   return gradeService.getStudentAverage(Integer.parseInt(parts[1]));
        if(command.equals("print_both"))   return gradeService.getCombinedInformation(Integer.parseInt(parts[1]));
        else return "Failed: Invalid command";
    }

}