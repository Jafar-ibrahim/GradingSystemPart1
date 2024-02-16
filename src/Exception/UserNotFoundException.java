package Exception;

import java.sql.SQLException;

public class UserNotFoundException extends SQLException {
    public UserNotFoundException() {
        super("User does not exist , try again with a valid id.");
    }
}
