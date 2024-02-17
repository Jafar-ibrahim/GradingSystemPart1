import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Client {

    private final String host;
    private final int port;
    private int userId;
    private int specificId;
    private String name;
    private int roleId;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        Client client = new Client(host,port);
        client.run();
    }
    public void run() {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {

            String username = getUserInput("Username: ");
            String password = getUserInput("Password: ");

            String message = username + "," + password;

            outputStream.writeUTF(message);
            outputStream.flush();

            String response = inputStream.readUTF();
            String[] info = response.split(",");
            if(info.length < 3){
                System.out.println(response);
            }else{
                userId = Integer.parseInt(info[0]);
                specificId = Integer.parseInt(info[1]);
                roleId = Integer.parseInt(info[2]);
                name = info[3];
                System.out.println(inputStream.readUTF());
                System.out.println(userId+","+specificId+","+roleId+","+name);
            }
            String command = "";
            while (true) {
                command = userInterface();

                outputStream.writeUTF(command);
                outputStream.flush();

                response = inputStream.readUTF();
                System.out.println("\n"+response);
                if (command.equals("Exit"))
                    break;

            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String getUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }
    private int getUserIntegerInput(String prompt) {
        System.out.print(prompt);
        return readIntegerInput(0,Integer.MAX_VALUE);
    }

    public final int readIntegerInput(int min , int max){
        Scanner scanner = new Scanner(System.in);
        int input ;
        while(true) {
            try {
                input = scanner.nextInt();
                if (input >= min && input <= max)
                    break;
                else
                    System.out.println("Please enter a number in the specified range("+min+"-"+max+")");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format , please enter a number :");
            }finally {
                scanner.nextLine();
            }
        }
        return input;
    }

    public String userInterface(){
        System.out.println("=========================================================================");
        System.out.println("Welcome to the Grading System, "+getRoleString(roleId)+" "+name+" !");
        System.out.println("Please choose an operation to perform from the menu :  ");
        if (roleId == 1){
            return printAdminOptions();
        } else if (roleId == 2) {
            return printInstructorOptions();
        } else{
            return printStudentOptions();
        }
    }
    public String printStudentOptions(){
        System.out.println("1- Print student grades");
        System.out.println("2- Print student grades average in all courses");
        System.out.println("3- Print Both");
        System.out.println("4- Exit");
        int student_id;
        if(roleId != 3) // if not a student , but admin
            student_id = getUserIntegerInput("Student ID: ");
        else
            student_id = specificId;
        int choice = readIntegerInput(1,4);
        String command ;
        if(choice == 1){
            command = "print_grades"+","+student_id;
        }else if(choice == 2){
            command = "print_average"+","+student_id;
        }else if(choice == 3)
            command = "print_both"+","+student_id;
        else
            command = "Exit";

        return command;
    }
    public String printInstructorOptions(){
        System.out.println("1- Print sections assigned to the instructor");
        System.out.println("2- Print student grades in a section");
        System.out.println("3- Add grade");
        System.out.println("4- delete grade");
        System.out.println("5- Modify grade");
        System.out.println("6- Exit");
        int instructor_id;

        if(roleId != 2) // if not an instructor , but admin
            instructor_id = getUserIntegerInput("Instructor ID: ");
        else
            instructor_id = specificId;
        int choice = readIntegerInput(1,6);
        String command ;
        if(choice == 1){
            command = "print_sections";
            command+= ","+instructor_id;
        }else if(choice == 2){
            command = "print_section_grades";
            command += ","+instructor_id+","+getUserIntegerInput("Section ID: ");
        }else if(choice == 3 || choice == 4 || choice == 5) {
            if (choice == 3)
                command = "add_grade";
            else if(choice == 4)
                command = "delete_grade";
            else
                command = "update_grade";
            command += "," + instructor_id + "," + getUserIntegerInput("Section ID: ");
            command += "," + getUserIntegerInput("Student ID: ");
            if(choice == 3 || choice == 5)
                command += "," + getUserIntegerInput("Grade: ");
        }else
            command = "Exit";

        return command;
    }
    public String printAdminOptions(){
        System.out.println(" 1- add course                      2- delete course ");
        System.out.println(" 3- add section                     4- delete section ");
        System.out.println(" 5- add instructor                  6- delete instructor ");
        System.out.println(" 7- add student                     8- delete student ");
        System.out.println(" 9- add student to section         10- remove student from section");
        System.out.println("11- add instructor to section      12- remove instructor from section");
        System.out.println("13- Exit");

        int choice = readIntegerInput(1,13);
        String command ;
        if(choice == 1){
            command = "add_course";
            command += ","+getUserInput("Course name: ");
        }else if(choice == 2 || choice == 3){
            if(choice == 2)
                command = "delete_course";
            else
                command = "add_section";
            command += ","+getUserIntegerInput("Course ID: ");
        }else if(choice == 4){
            command = "delete_section";
            command += ","+getUserIntegerInput("Section ID: ");
        }else if(choice == 5 || choice == 7){
            if(choice == 5)
                command = "add_instructor";
            else
                command = "add_student";
            command += ","+getUserInput("Username: ");
            command += ","+getUserInput("Password: ");
            command += ","+getUserInput("First Name: ");
            command += ","+getUserInput("Last Name: ");
        }else if(choice == 6){
            command = "delete_instructor";
            command += ","+getUserIntegerInput("Instructor ID: ");
        }else if(choice == 8){
            command = "delete_student";
            command += ","+getUserIntegerInput("Student ID: ");
        }else if(choice == 9 || choice == 10){
            if(choice == 9)
                command = "add_student_section";
            else
                command = "remove_student_section";
            command += ","+getUserIntegerInput("Student ID: ");
            command += ","+getUserIntegerInput("Section ID: ");
        }else if(choice == 11 || choice == 12){
            if(choice == 11)
                command = "add_instructor_section";
            else
                command = "remove_instructor_section";
            command += ","+getUserIntegerInput("Instructor ID: ");
            command += ","+getUserIntegerInput("Section ID: ");
        }else
            return "Exit";

        return command;
    }
    public String getRoleString(int roleId){
        if(roleId == 1) return "Admin";
        else if(roleId == 2) return "Instructor";
        else if (roleId == 3) return "Student";
        else return "N/A";
    }

}
