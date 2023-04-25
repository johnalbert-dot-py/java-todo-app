package todoapplication.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import todoapplication.core.SQLConnection;
import todoapplication.utilities.Hashing;

// import bcrypt for password hashing

public class User extends SQLConnection {

  private Connection sqlConnection;
  private final String TABLE_NAME = "User";

  public User() {
    try {
      this.sqlConnection = connect();
    } catch (Exception e) {
      throw new RuntimeException("Connection to database failed");
    }
  }

  public int createUser(String username, String password) throws Exception {

    String strongPassword = Hashing.get_SHA_256_SecurePassword(password);
    String query = "INSERT INTO " + TABLE_NAME
        + " (username, password) VALUES (?, ?)";

    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query,
          Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, strongPassword);
      int affectedRows = preparedStatement.executeUpdate();
      // get the created id from the database

      if (affectedRows == 0) {
        throw new Exception("Creating user failed, no rows affected.");
      }

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int id = generatedKeys.getInt(1);
          generatedKeys.close();
          preparedStatement.close();
          return id;
        } else {
          throw new Exception("Creating user failed, no ID obtained.");
        }
      }

    } catch (SQLException e) {
      throw new Exception("There was an error on communicating with the database: " + e.getMessage());
    }
  }

  public String validateUserCredentials(String username, String password) {
    String protectedPassword = Hashing.get_SHA_256_SecurePassword(password);
    String query = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, protectedPassword);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString("id");
      }
      return "Invalid credentials.";
    } catch (SQLException e) {
      return "There was an error on communicating with the database: " + e.getMessage();
    }
  }

}
