/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package todoapplication.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import todoapplication.utilities.ToDoItemContainer;
import todoapplication.utilities.TodoAlert;
import todoapplication.data.ToDo;
import todoapplication.singleton.UserSession;
import todoapplication.types.ToDoItem;

/**
 *
 * @author John Albert Flores
 */
public class MyTasksController implements Initializable {

    private Stage stage;

    @FXML
    private VBox toDoItemLists;

    @FXML
    private Button completedTaskBtn;

    @FXML
    private Button pendingTaskBtn;

    @FXML
    private Button addNewTaskBtn;

    @FXML
    private TextField titleInput;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private DatePicker dueDateInput;

    @FXML
    public void changePage(ActionEvent event) {
        System.out.println("Triggered " + event);
        String btnText = ((Button) event.getSource()).getText();

        if (btnText.equals("My Tasks")) {
            toDoItemLists.getChildren().clear();
            addNewTaskBtn.setText("Add New Task");
            // remove active class on pending task
            completedTaskBtn.getStyleClass().remove("page-active");
            pendingTaskBtn.getStyleClass().add("page-active");
            // add active class on completed task
            addTodoItems(false);
            toDoItemLists.getChildren().add(addNewTaskBtn);

        } else if (btnText.equals("Completed Tasks")) {
            // clear the container
            toDoItemLists.getChildren().clear();
            // remove active class on pending task
            pendingTaskBtn.getStyleClass().remove("page-active");
            // add active class on completed task
            completedTaskBtn.getStyleClass().add("page-active");
            addTodoItems(true);

        } else if (btnText.equals("Logout")) {
            UserSession.destroy();
            try {
                this.changeToLoginPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addNewTask(ActionEvent event) {
        System.out.println("Triggered " + event);
        String btnText = ((Button) event.getSource()).getText();

        if (btnText.equals("Save Task")) {

            boolean isAdded = ToDoItemContainer.addNewTask(toDoItemLists, titleInput, descriptionInput, dueDateInput);
            addNewTaskBtn.setText("Add New Task");
            if (!isAdded) {
                // show alert
                TodoAlert.showAlert(AlertType.ERROR, "Error", "Error", "Please check all the fields.");
                this.titleInput = new TextField();
                this.descriptionInput = new TextArea();
            }
        } else {
            this.titleInput = new TextField();
            this.descriptionInput = new TextArea();
            this.dueDateInput = new DatePicker();
            ToDoItemContainer.insertAddNewTaskForm(toDoItemLists, titleInput, descriptionInput, dueDateInput, "0");
            addNewTaskBtn.setText("Save Task");
        }
    }

    @FXML
    private void changeToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader myTasksPage = new FXMLLoader(getClass().getResource("../screens/LoginScreen.fxml"));
        myTasksPage.setRoot(new BorderPane());
        Parent root = myTasksPage.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // fetching data for
        System.out.println("Fetching data for user id " + UserSession.getUserId());
        addTodoItems(false);

    }

    public void addTodoItems(boolean onlyCompleted) {
        ToDo todo = new ToDo();

        ArrayList<ToDoItem> toDoItems;
        if (onlyCompleted) {
            toDoItems = todo.getAllToDoByUserId(UserSession.getUserId(), true);
        } else {
            toDoItems = todo.getAllToDoByUserId(UserSession.getUserId(), false);
        }

        if (toDoItems.size() == 0) {
            ToDoItemContainer.insertNoTaskMessage(toDoItemLists);
        }

        for (ToDoItem toDoItem : toDoItems) {
            ToDoItemContainer.insertTodoContainer(toDoItemLists, toDoItem.title, toDoItem.description,
                    toDoItem.due_date, String.valueOf(toDoItem.id), toDoItem.completed, 0);
        }
    }

}
