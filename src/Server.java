import Service.*;
import Util.SchemaManager;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server  {

    private int port;
    private  DataSource dataSource;
    private SchemaManager schemaManager;

    public Server(int port) {
        this.port = port;
        try {
            dataSource = getDataSource();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        schemaManager = new SchemaManager(dataSource);;
    }

    public static void main(String[] args) throws SQLException, IOException {
        int port = 7070;
        Server server = new Server(port);
        server.schemaManager.dropAllTablesIfExist();
        server.schemaManager.initializeTables();

        server.start();
    }

    public void start() throws IOException {
        // to limit the number of sessions opened
        ExecutorService executor = Executors.newFixedThreadPool(30);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started at port: "+port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new thread for handling the client
                executor.execute(new Thread(new ClientHandler(clientSocket,dataSource)));
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
        ds.setPassword("qwerty");
        ds.setUseSSL(false);
        ds.setAllowPublicKeyRetrieval(true);

        return ds;
    }

}
