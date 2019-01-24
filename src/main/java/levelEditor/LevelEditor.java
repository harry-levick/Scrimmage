package levelEditor;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import shared.gameObjects.ExampleObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Version;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.MapLoader;

public class LevelEditor extends Application {

  private ArrayList<GameObject> gameObjects;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Level Editor");
    Group root = new Group();
    gameObjects = new ArrayList<>();

    // Example of loading map
    // gameObjects = MapLoader.loadMap("menus.map");
    gameObjects.forEach(gameObject -> gameObject.initialise(root, Version.CLIENT, false));

    ChoiceBox cb = new ChoiceBox();
    cb.setItems(FXCollections.observableArrayList("ExampleObject", "Player"));
    Button btn = new Button();
    btn.setText("Save Map");
    btn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            MapLoader.saveMap(gameObjects, "menus.map");
          }
        });
    btn.setLayoutX(10);
    btn.setLayoutY(10);

    root.getChildren().add(cb);
    root.getChildren().add(btn);

    Scene scene = new Scene(root, 1000, 1000);
    scene.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (cb.getValue() == "ExampleObject") {
              GameObject temp = new ExampleObject(event.getX(), event.getY(), ObjectID.Bot);
              temp.initialise(root, Version.CLIENT, false);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Player") {
              Player temp = new Player(event.getX(), event.getY(), ObjectID.Player);
              temp.initialise(root, Version.CLIENT, false);
              gameObjects.add(temp);
            }
          }
        });
    primaryStage.setScene(scene);
    primaryStage.show();
    System.out.println("testasd");

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        gameObjects.forEach(gameObject -> gameObject.render());
      }
    }.start();
  }
}
