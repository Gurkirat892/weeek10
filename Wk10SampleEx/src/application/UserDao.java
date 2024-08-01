package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public boolean userExists(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT username, lastname, firstname, password, email, gender, dateofbirth, course FROM user WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString(1));
                user.setLastName(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                users.add(user);
            }
            return !users.isEmpty();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return !users.isEmpty();
    }

    public int saveUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO user(username, lastname, firstname, password, email, dateofbirth, gender, course) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            statement.setString(counter++, user.getUsername());
            statement.setString(counter++, user.getLastName());
            statement.setString(counter++, user.getFirstName());
            statement.setString(counter++, user.getPassword());
            statement.setString(counter++, user.getEmail());
            statement.setString(counter++, user.getDateOfBirth());
            statement.setString(counter++, user.getGender());
            statement.setString(counter++, user.getCourse());
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return 0;
    }
}