package todoapplication.utilities;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class TodoAlert {
  static public void showAlert(AlertType type, String title, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }

  static public void showAlert(AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle("Error");
    alert.setHeaderText("Error adding new task");
    alert.setContentText("Please fill out all fields");
    alert.showAndWait();
  }
}
