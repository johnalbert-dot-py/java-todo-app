package todoapplication.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import todoapplication.data.User;
import todoapplication.singleton.UserSession;
import todoapplication.utilities.TodoAlert;;

public class SignUpController implements Initializable {

  private Stage stage;

  @FXML
  private VBox container;

  @FXML
  private Button loginBtn;

  @FXML
  private Button signUpButton;

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField confirmPasswordField;

  @FXML
  private Label loginError;

  @FXML
  void signUp(ActionEvent event) {
    System.out.println("From Log In Controller " +
        UserSession.getUserId());
    boolean loggedIn = false;
    if (!passwordField.getText().equals(confirmPasswordField.getText())) {
      loginError.setVisible(true);
      loginError.setText("Password does not match.");
      try {
        container.getChildren().add(1, loginError);
      } catch (Exception e) {
        // System.out.println("Error: " + e.getMessage());
      }
      return;
    }

    loginError.setVisible(true);

    try {
      container.getChildren().add(1, loginError);
    } catch (Exception e) {
      // System.out.println("Error: " + e.getMessage());
    }

    String username = usernameField.getText();
    String password = passwordField.getText();
    User user = new User();

    if (username != "" && password != "") {

      try {
        int created = user.createUser(username, password);
        if (created > 0) {
          loginError.getStyleClass().add("success");
          loginError.setText("Sign Up Success.");
          loggedIn = true;
        } else {
          String message = "Something went wrong.";
          loginError.setText(message);
        }
      } catch (Exception e) {
        String message = e.getMessage();
        loginError.setText("Error Occured on database");
        TodoAlert.showAlert(AlertType.ERROR, "MySQL Error", "Error occured", message);
      }

    } else {
      String message = "Please check your fields.";
      loginError.setText(message);
    }

    if (loggedIn) {
      try {
      } catch (Exception e) {
      }
    }

  }

  @FXML
  void changeScreenToLogin(ActionEvent event) throws IOException {
    FXMLLoader loginPage = new FXMLLoader(getClass().getResource("../screens/LoginScreen.fxml"));
    loginPage.setRoot(new BorderPane());
    Parent root = loginPage.load();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    container.getChildren().remove(1);
  }

  public void initData() {

  }

}
