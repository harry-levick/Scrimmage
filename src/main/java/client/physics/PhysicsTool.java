package client.physics;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PhysicsTool extends Application {

  Group root;

  @Override
  public void start(Stage primaryStage) {
    setUpStage(primaryStage);
  }

  void setUpStage(Stage primaryStage) {
    primaryStage.setTitle("Physics Experiment");
    root = new Group();
    Label label1 = new Label("Name:");
    TextField textField = new TextField();
    HBox hb = new HBox();
    hb.getChildren().addAll(label1, textField);
    hb.setSpacing(10);
    hb.setLayoutX(100);
    hb.setLayoutY(200);
    root.getChildren().add(hb);
    Scene scene = new Scene(root, 1280, 720);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
