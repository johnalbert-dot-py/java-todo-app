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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import todoapplication.singleton.UserSession;
import todoapplication.data.User;

// import todoapplication.controllers.SignUpController;

public class LoginController implements Initializable {

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
  private Label loginError;

  @FXML
  void login(ActionEvent event) {

    String username = usernameField.getText();
    String password = passwordField.getText();

    User user = new User();
    String userValidate = user.validateUserCredentials(username, password);

    // try to convert to int

    try {
      int userId = Integer.parseInt(userValidate);
      UserSession.setUserId(userId);
      try {
        this.changeScreenToMyTasks(event);
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    } catch (NumberFormatException error) {
      loginError.setVisible(true);
      loginError.setText(userValidate);
      try {
        container.getChildren().add(1, loginError);
      } catch (Exception e) {
        // System.out.println("Error: " + e.getMessage());
      }
    }

  }

  @FXML
  private void changeScreenToSignUp(ActionEvent event) throws IOException {
    FXMLLoader signUpPage = new FXMLLoader(getClass().getResource("../screens/SignUpScreen.fxml"));
    signUpPage.setRoot(new BorderPane());
    Parent root = signUpPage.load();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void changeScreenToMyTasks(ActionEvent event) throws IOException {
    FXMLLoader myTasksPage = new FXMLLoader(getClass().getResource("../screens/MyTasksScreen.fxml"));
    myTasksPage.setRoot(new BorderPane());
    Parent root = myTasksPage.load();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    // remove the label
    container.getChildren().remove(1);
    // loginError.setVisible(false);
  }
}
