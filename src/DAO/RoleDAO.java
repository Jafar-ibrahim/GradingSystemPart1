package DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoleDAO {

    private final DataSource dataSource;

    public RoleDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRole(Connection connection, int roleId , String roleName) throws SQLException {
        String sql = "INSERT INTO role(role_id, name) VALUES (?,?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roleId);
            preparedStatement.setString(2, roleName);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRole(int roleId) throws SQLException {
        String sql = "DELETE FROM role WHERE role_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roleId);
            preparedStatement.executeUpdate();
        }
    }

}
