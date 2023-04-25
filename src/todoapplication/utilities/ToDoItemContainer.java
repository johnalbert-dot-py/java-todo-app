package todoapplication.utilities;

import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import todoapplication.data.ToDo;
import todoapplication.singleton.UserSession;
import todoapplication.types.ToDoItem;

public class ToDoItemContainer {

  static public String insertTodoContainer(VBox toDoItemLists, String title, String description, String dueDate,
      String id, boolean checked, int index) {

    VBox todoContainer = new VBox();
    todoContainer.getStyleClass().add("todo-item");

    HBox todoItemFlex = new HBox();
    todoItemFlex.getStyleClass().add("todo-item--flex");

    // // // add contents
    VBox todoItemContent = new VBox();

    CheckBox todoCheckbox = new CheckBox();
    Button deleteButton = new Button("Delete");
    Button updateButton = new Button("Update");
    deleteButton.getStyleClass().add("delete-btn");
    updateButton.getStyleClass().add("update-btn");

    if (checked) {
      todoCheckbox.setSelected(true);
    }

    Label newTitle = new Label(title);
    Label newDescription = new Label(description);
    Text newDueDate = new Text("Due on: " + dueDate);
    newTitle.getStyleClass().add("todo-item--title");
    newDescription.getStyleClass().add("todo-item--description");
    newDueDate.getStyleClass().add("todo-item--date");

    if (newDescription.getText().length() >= 60) {
      System.out.println("has ellipsis");
    }

    todoItemContent.getChildren().add(newTitle);
    todoItemContent.getChildren().add(newDescription);
    todoItemContent.getChildren().add(newDueDate);

    todoItemContent.prefWidthProperty().bind(todoContainer.widthProperty().subtract(300));

    todoItemFlex.getChildren().add(todoCheckbox);
    todoItemFlex.getChildren().add(todoItemContent);
    todoItemFlex.getChildren().add(updateButton);
    todoItemFlex.getChildren().add(deleteButton);

    todoContainer.getChildren().add(todoItemFlex);
    todoContainer.setId(id);

    toDoItemLists.getChildren().add(index, todoContainer);

    updateButton.setOnAction((event) -> {
      System.out.println("Index of current container:" + toDoItemLists.getChildren().indexOf(todoContainer));
      TextField titleInput = new TextField(title);
      TextArea descriptionInput = new TextArea(description);
      DatePicker dueDateInput = new DatePicker();
      dueDateInput.setValue(LocalDate.parse(dueDate));
      updateTaskForm(toDoItemLists, titleInput, descriptionInput, dueDateInput, id,
          toDoItemLists.getChildren().indexOf(todoContainer));
    });

    deleteButton.setOnAction(event -> {
      System.out.println("Delete button clicked");
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Delete Task");
      alert.setHeaderText("Are you sure you want to delete this task?");
      alert.setContentText("This action cannot be undone");

      alert.showAndWait().ifPresent(response -> {
        if (response == javafx.scene.control.ButtonType.OK) {
          ToDo todo = new ToDo();
          todo.deleteTodoById(Integer.parseInt(id));
          toDoItemLists.getChildren().remove(todoContainer);
          if (toDoItemLists.getChildren().size() <= 1) {
            insertNoTaskMessage(toDoItemLists);
          }
        }
      });
    });

    todoCheckbox.setOnAction((event) -> {
      System.out.println("Checkbox for id" + id + " clicked");
      System.out.println("Checkbox changed: " + todoCheckbox.isSelected());
      ToDo todo = new ToDo();
      if (todoCheckbox.isSelected()) {
        todo.setToDoAsDone(Integer.parseInt(id));
        // remove
        toDoItemLists.getChildren().remove(todoContainer);
        if (toDoItemLists.getChildren().size() <= 1) {
          insertNoTaskMessage(toDoItemLists);
        }
      }
    });

    return todoContainer.getId();
  }

  static public void insertNoTaskMessage(VBox toDoItemLists) {
    Text noTaskMessage = new Text("No tasks yet!");
    noTaskMessage.getStyleClass().add("no-tasks-text");

    toDoItemLists.getChildren().add(0, noTaskMessage);
    // toDoItemLists.getChildren().add(createAddBtn(eventHandler));
  }

  static public VBox createAddNewTaskForm(VBox toDoItemsLists, TextField titleInput, TextArea descriptionInput,
      DatePicker dueDateInput, String id) {
    VBox todoContainer = new VBox();
    todoContainer.getStyleClass().add("todo-item");

    titleInput.getStyleClass().add("todo-item--input");
    descriptionInput.getStyleClass().add("todo-item--textarea");
    descriptionInput.setPrefRowCount(5);

    titleInput.setPromptText("Enter Task title");
    descriptionInput.setPromptText("Enter Task description");
    dueDateInput.setPromptText("Enter Task due date");

    titleInput.setFocusTraversable(true);
    descriptionInput.setFocusTraversable(true);
    dueDateInput.setFocusTraversable(true);

    dueDateInput.setDayCellFactory(param -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        setDisable(empty || date.compareTo(LocalDate.now()) < 0);
      }
    });

    todoContainer.setId(id);
    todoContainer.getChildren().add(titleInput);
    todoContainer.getChildren().add(descriptionInput);
    todoContainer.getChildren().add(dueDateInput);
    return todoContainer;

  }

  static public void insertAddNewTaskForm(VBox toDoItemsLists, TextField titleInput, TextArea descriptionInput,
      DatePicker dueDateInput, String id) {
    VBox todoContainer = createAddNewTaskForm(toDoItemsLists, titleInput, descriptionInput, dueDateInput, id);
    toDoItemsLists.getChildren().add(0, todoContainer);
  }

  static public void updateTaskForm(VBox toDoItemsLists, TextField titleInput, TextArea descriptionInput,
      DatePicker dueDateInput, String id, int index) {

    Button updateBtn = new Button("Save");
    updateBtn.getStyleClass().add("update-btn");

    VBox todoContainer = createAddNewTaskForm(toDoItemsLists, titleInput, descriptionInput, dueDateInput, id);
    todoContainer.getChildren().add(updateBtn);
    toDoItemsLists.getChildren().remove(index);
    toDoItemsLists.getChildren().add(index, todoContainer);
    updateBtn.setOnAction(event -> {
      System.out.println("Update button clicked");
      System.out.println("Title: " + titleInput.getText());
      System.out.println("Description: " + descriptionInput.getText());
      System.out.println("Due Date: " + dueDateInput.getValue());

      ToDo todo = new ToDo();
      ToDoItem updatedTodoItem = new ToDoItem();
      updatedTodoItem.title = titleInput.getText();
      updatedTodoItem.description = descriptionInput.getText();
      updatedTodoItem.due_date = dueDateInput.getValue().toString();
      updatedTodoItem.id = Integer.parseInt(todoContainer.getId());
      todo.updateTodo(updatedTodoItem);

      // todo implement updated on back-end

      addNewTask(toDoItemsLists, titleInput, descriptionInput, dueDateInput, index);
    });
  }

  static public boolean addNewTask(VBox toDoItemLists, TextField titleInput,
      TextArea descriptionInput, DatePicker dueDateInput) {

    // remove the first on the list
    toDoItemLists.getChildren().remove(0);

    // add the new one
    String title = titleInput.getText();
    String description = descriptionInput.getText();
    LocalDate dueDate = dueDateInput.getValue();

    // validate due date
    if (dueDate == null) {
      return false;
    }

    if (title.length() <= 0 || description.length() <= 0) {
      return false;
    }

    // TODO: implement database insert

    ToDo todo = new ToDo();
    int id = todo.createTodo(title, description, dueDate.toString(), false, UserSession.getUserId());

    ToDoItemContainer.insertTodoContainer(toDoItemLists, title,
        description,
        dueDate.toString(),
        String.valueOf(id),
        false,
        0);

    return true;
  }

  static public boolean addNewTask(VBox toDoItemLists, TextField titleInput,
      TextArea descriptionInput, DatePicker dueDateInput, int index) {

    // remove the container using the index on the list
    toDoItemLists.getChildren().remove(index);

    // add the new one
    String title = titleInput.getText();
    String description = descriptionInput.getText();
    LocalDate dueDate = dueDateInput.getValue();

    // validate due date
    if (dueDate == null) {
      return false;
    }

    if (title.length() <= 0 || description.length() <= 0) {
      return false;
    }

    ToDo todo = new ToDo();
    int id = todo.createTodo(title, description, dueDate.toString(), false, UserSession.getUserId());

    ToDoItemContainer.insertTodoContainer(toDoItemLists, title,
        description,
        dueDate.toString(),
        String.valueOf(id),
        false,
        index);

    return true;
  }

}
