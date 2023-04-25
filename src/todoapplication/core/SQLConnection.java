package todoapplication.core;

import java.sql.*;

public class SQLConnection {

  public static Connection connect() {
    Connection connection = null;
    try {

      // Database URL
      String DB_URL = "jdbc:mysql://localhost:3306/";
      // Database name
      String DB_NAME = "TodoApplicationDB";
      // Database username
      String DB_USERNAME = "root";
      // Database password
      String DB_PASSWORD = "";
      connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USERNAME, DB_PASSWORD);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return connection;
  }

}
