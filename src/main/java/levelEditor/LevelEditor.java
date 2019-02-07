package levelEditor;

import java.util.ArrayList;
import java.util.UUID;
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
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import shared.gameObjects.ExampleObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.main.ButtonLeveleditor;
import shared.gameObjects.menu.main.ButtonMultiplayer;
import shared.gameObjects.menu.main.ButtonSettings;
import shared.gameObjects.menu.main.ButtonSingleplayer;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Handgun;
import shared.handlers.levelHandler.MapLoader;

public class LevelEditor extends Application {

  private ArrayList<GameObject> gameObjects;
  private boolean snapToGrid = true;

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
    gameObjects.forEach(gameObject -> gameObject.initialise(root));

    ChoiceBox cb = new ChoiceBox();
    cb.setItems(
        FXCollections.observableArrayList(
            "ExampleObject",
            "Player",
            "Singleplayer Button",
            "Multiplayer Button",
            "Settings Button",
            "Level Editor Button",
            "Handgun"));
    cb.setLayoutX(10);
    cb.setLayoutY(10);

    Button btnSave = new Button();
    btnSave.setText("Save Map");
    btnSave.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            MapLoader.saveMap(gameObjects, "menu.map");
          }
        });
    btnSave.setLayoutX(160);
    btnSave.setLayoutY(10);

    Button btnToggleGrid = new Button();
    btnToggleGrid.setText("Toggle Snap to Grid");
    btnToggleGrid.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            snapToGrid = !snapToGrid;
            ArrayList<Line> gridlines = redrawGrid();
            for (Line line : gridlines) {
              root.getChildren().add(line);
            }
          }
        });
    btnToggleGrid.setLayoutX(250);
    btnToggleGrid.setLayoutY(10);

    ArrayList<Line> gridlines = redrawGrid();
    for (Line line : gridlines) {
      root.getChildren().add(line);
    } // todo remove

    root.getChildren().add(cb);
    root.getChildren().add(btnSave);
    root.getChildren().add(btnToggleGrid);

    Scene scene = new Scene(root, 1920, 1080);
    scene.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            UUID uuid = UUID.randomUUID();
            if (cb.getValue() == "ExampleObject") {
              GameObject temp =
                  new ExampleObject(event.getX(), event.getY(), 100, 100, ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Player") {
              Player temp = new Player(event.getX(), event.getY(), 100, 100, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Singleplayer Button") {
              ButtonSingleplayer temp =
                  new ButtonSingleplayer(event.getX(), event.getY(), 100, 100, ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Multiplayer Button") {
              ButtonMultiplayer temp =
                  new ButtonMultiplayer(event.getX(), event.getY(), 100, 100, ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Settings Button") {
              ButtonSettings temp =
                  new ButtonSettings(event.getX(), event.getY(), 100, 100, ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Level Editor Button") {
              ButtonLeveleditor temp =
                  new ButtonLeveleditor(event.getX(), event.getY(), 100, 100, ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Handgun") {
              Handgun temp =
                  new Handgun(
                      event.getX(),
                      event.getY(),
                      100,
                      100,
                      ObjectID.Weapon,
                      10,
                      10,
                      "Handgun",
                      100,
                      100,
                      100,
                      10,
                      uuid);
              temp.initialise(root);
            }
          }
        });
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setFullScreen(true);

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        gameObjects.forEach(gameObject -> gameObject.render());
      }
    }.start();
  }

  private ArrayList<Line> redrawGrid() {
    // sets 10x10 grid based on scene size
    int sceneX = 1920; // size of scene TODO fetch automatically
    int sceneY = 1080;
    int gridX = 20;
    int gridY = 20;

    ArrayList<Line> gridlines = new ArrayList<Line>();
    if (snapToGrid) {
      for (int i = 0; i < gridX; i++) {
        int xPos = (sceneX / gridX) * i;
        Line line = new Line(xPos, 0, xPos, 1080);
        gridlines.add(line);
      }
      for (int i = 0; i < gridY; i++) {
        int yPos = (sceneY / gridY) * i;
        Line line = new Line(0, yPos, 1920, yPos);
        gridlines.add(line);
      }
    }

    return gridlines;
  }
}
