package DAO;

import javax.sql.DataSource;
import java.sql.*;


public class AdminDAO {

    private final DataSource dataSource;

    public AdminDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void insertAdmin(int userId) throws SQLException {
        String sql = "INSERT INTO admin(user_id) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAdmin(int adminId) throws SQLException {
        String sql = "DELETE FROM admin WHERE admin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, adminId);
            preparedStatement.executeUpdate();
        }
    }

    public int getAdminId(int userId) throws SQLException {
        String sql = "SELECT admin_id FROM admin WHERE user_id = ?";
        int adminId = -1; // Default value if admin not found

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    adminId = resultSet.getInt("admin_id");
                }
            }

        }

        return adminId;
    }

}
