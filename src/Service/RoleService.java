package Service;

import DAO.RoleDAO;

import javax.sql.DataSource;
import java.sql.SQLException;

public class RoleService {

    private final RoleDAO roleDAO;

    public RoleService(DataSource dataSource) {
        roleDAO = new RoleDAO(dataSource);
    }
    public void addRole(int roleId,String roleName){
        try{
            roleDAO.insertRole(roleId,roleName);
        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }

    public void deleteRole(int roleId){
        try{
            roleDAO.deleteRole(roleId);
        }catch (SQLException e){
            System.out.println(e.getStackTrace());
        }
    }
}
