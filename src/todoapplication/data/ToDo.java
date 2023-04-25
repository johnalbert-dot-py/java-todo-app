package todoapplication.data;

import todoapplication.core.SQLConnection;
import java.sql.*;
import java.util.*;

import todoapplication.types.ToDoItem;

public class ToDo extends SQLConnection {

  /*
   * 
   * ToDo Class
   * This
   * class should have the following attributes
   * - id
   * - title
   * - description
   * - due_date
   * - completed
   * - created_at
   * - updated_at
   * - user_id
   */

  private Connection sqlConnection;
  private final String TABLE_NAME = "Todo";

  public ToDo() {
    try {
      this.sqlConnection = connect();
    } catch (Exception e) {
      throw new RuntimeException("Connection to database failed");
    }
  }

  public int createTodo(
      String title,
      String description,
      String due_date,
      boolean completed,
      int user_id) {
    String query = "INSERT INTO " + TABLE_NAME
        + " (title, description, due_date, is_completed, user_id) VALUES (?, ?, ?, ?, ?)";
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query,
          Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, title);
      preparedStatement.setString(2, description);
      preparedStatement.setString(3, due_date);
      preparedStatement.setBoolean(4, completed);
      preparedStatement.setInt(5, user_id);
      int affectedRows = preparedStatement.executeUpdate();
      // get the created id from the database

      if (affectedRows == 0) {
        System.out.println("Creating todo failed, no rows affected.");
        return 0;
      }

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int id = generatedKeys.getInt(1);
          generatedKeys.close();
          preparedStatement.close();
          return id;
        } else {
          System.out.println("Creating todo failed, no ID obtained.");
          return 0;
        }
      }

    } catch (SQLException e) {
      System.out.println("There was an error on communicating with the database: " + e.getMessage());
      return 0;
    }

  }

  public ToDoItem getToDoById(int id) {

    String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        ToDoItem todo = new ToDoItem(
            resultSet.getInt("id"),
            resultSet.getString("title"),
            resultSet.getString("description"),
            resultSet.getString("due_date"),
            resultSet.getBoolean("is_completed"),
            resultSet.getString("created_at"),
            resultSet.getString("updated_at"),
            resultSet.getInt("user_id"));
        return todo;
      }
      resultSet.close();
      preparedStatement.close();
      return null;
    } catch (SQLException e) {
      System.out.println("Error occured when getting Todo by ID: " + e.getMessage());
      return null;
    }

  }

  public ToDoItem updateTodo(ToDoItem todoItem) {
    String query = "UPDATE " + TABLE_NAME
        + " SET title = ?, description = ?, due_date = ? WHERE id = ?";
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setString(1, todoItem.title);
      preparedStatement.setString(2, todoItem.description);
      preparedStatement.setString(3, todoItem.due_date);
      preparedStatement.setInt(4, todoItem.id);
      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows == 0) {
        System.out.println("Updating todo failed, no rows affected.");
        return null;
      }
      preparedStatement.close();
      return getToDoById(todoItem.id);
    } catch (SQLException e) {
      System.out.println("There was an error on communicating with the database");
      return null;
    }
  }

  public ArrayList<ToDoItem> getAllToDoByUserId(int userId) {
    String query = "SELECT * FROM " + TABLE_NAME + " INNER JOIN User ON Todo.user_id = User.id WHERE User.id = ? ";
    ArrayList<ToDoItem> todoList = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setInt(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        ToDoItem todo = new ToDoItem(
            resultSet.getInt("id"),
            resultSet.getString("title"),
            resultSet.getString("description"),
            resultSet.getString("due_date"),
            resultSet.getBoolean("is_completed"),
            resultSet.getString("created_at"),
            resultSet.getString("updated_at"),
            resultSet.getInt("user_id"));
        todoList.add(todo);
      }
      resultSet.close();
      preparedStatement.close();
      return todoList;
    } catch (SQLException e) {
      System.out.println("Error occured when getting Todo by ID: " + e.getMessage());
      return null;
    }
  }

  public ArrayList<ToDoItem> getAllToDoByUserId(int userId, boolean completed) {
    String query = "SELECT * FROM " + TABLE_NAME
        + " INNER JOIN User ON Todo.user_id = User.id WHERE User.id = ? AND Todo.is_completed = ? ";
    ArrayList<ToDoItem> todoList = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setInt(1, userId);
      preparedStatement.setBoolean(2, completed);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        ToDoItem todo = new ToDoItem(
            resultSet.getInt("id"),
            resultSet.getString("title"),
            resultSet.getString("description"),
            resultSet.getString("due_date"),
            resultSet.getBoolean("is_completed"),
            resultSet.getString("created_at"),
            resultSet.getString("updated_at"),
            resultSet.getInt("user_id"));
        todoList.add(todo);
      }
      resultSet.close();
      preparedStatement.close();
      return todoList;
    } catch (SQLException e) {
      System.out.println("Error occured when getting Todo by ID: " + e.getMessage());
      return null;
    }
  }

  public boolean deleteTodoById(int id) {
    String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      int affectedRows = preparedStatement.executeUpdate();
      preparedStatement.close();
      if (affectedRows == 0) {
        System.out.println("Deleting todo failed, no rows affected.");
        return false;
      }
      return true;
    } catch (SQLException e) {
      System.out.println("There was an error on communicating with the database");
      return false;
    }
  }

  public boolean setToDoAsDone(int id) {
    String query = "UPDATE " + TABLE_NAME + " SET is_completed = ? WHERE id = ?";
    try {
      PreparedStatement preparedStatement = sqlConnection.prepareStatement(query);
      preparedStatement.setBoolean(1, true);
      preparedStatement.setInt(2, id);
      int affectedRows = preparedStatement.executeUpdate();

      preparedStatement.close();

      if (affectedRows == 0) {
        System.out.println("Updating ToDo Failed, no rows affected.");
      }

      return true;

    } catch (SQLException e) {
      System.out.println("There was an error on communicating with the database");
      return false;
    }
  }

}
