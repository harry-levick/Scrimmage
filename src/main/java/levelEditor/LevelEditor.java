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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.gameObjects.ExampleObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.main.ButtonLeveleditor;
import shared.gameObjects.menu.main.ButtonMultiplayer;
import shared.gameObjects.menu.main.ButtonSettings;
import shared.gameObjects.menu.main.ButtonSingleplayer;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Handgun;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.MapLoader;

public class LevelEditor extends Application {

  private ArrayList<GameObject> gameObjects;
  private ArrayList<Player> playerSpawns = new ArrayList<Player>();
  private MapDataObject mapDataObject;
  private boolean snapToGrid = true;
  private ChoiceBox cb = new ChoiceBox();

  private int spawnPointLimit = 4; //todo autofetch

  private int stageSizeX = 1920; //todo autofetch
  private int stageSizeY = 1080;
  private int gridSizePX = 40;
  private int gridSizeX = stageSizeX / gridSizePX; //40 px blocks
  private int gridSizeY = stageSizeY / gridSizePX;



  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Level Editor");
    Group root = new Group();
    initialiseNewMap();

    // Example of loading map
    // gameObjects = MapLoader.loadMap("menus.map");
    //gameObjects.forEach(gameObject -> gameObject.initialise(root));

    addButtons(root);

    Scene scene = new Scene(root, stageSizeX, stageSizeY);
    scene.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            UUID uuid = UUID.randomUUID();
            if (cb.getValue() == "ExampleObject") {
              GameObject temp =
                  new ExampleObject(getGridX(event.getX()), getGridY(event.getY()), 100, 100,
                      ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Player Spawn Point") {
              if (mapDataObject.getSpawnPoints().size() < spawnPointLimit) {
                Player temp = new Player(getGridX(event.getX()), getGridY(event.getY()), 100, 100,
                    uuid);
                temp.initialise(root);
                playerSpawns.add(temp);
                mapDataObject.addSpawnPoint(getGridX(event.getX()), getGridY(event.getY()));
              } else {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Text text = new Text("\n\tWarning: You cannot create more than " + spawnPointLimit
                    + " spawn points.");
                dialogVbox.getChildren().add(text);
                Scene dialogScene = new Scene(dialogVbox, 450, 60);
                dialog.setScene(dialogScene);
                dialog.show();
              }

            } else if (cb.getValue() == "Singleplayer Button") {
              ButtonSingleplayer temp =
                  new ButtonSingleplayer(getGridX(event.getX()), getGridY(event.getY()), 100, 100,
                      ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Multiplayer Button") {
              ButtonMultiplayer temp =
                  new ButtonMultiplayer(getGridX(event.getX()), getGridY(event.getY()), 100, 100,
                      ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Settings Button") {
              ButtonSettings temp =
                  new ButtonSettings(getGridX(event.getX()), getGridY(event.getY()), 100, 100,
                      ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Level Editor Button") {
              ButtonLeveleditor temp =
                  new ButtonLeveleditor(getGridX(event.getX()), getGridY(event.getY()), 100, 100,
                      ObjectID.Bot, uuid);
              temp.initialise(root);
              gameObjects.add(temp);
            } else if (cb.getValue() == "Handgun") {
              Handgun temp =
                  new Handgun(
                      getGridX(event.getX()),
                      getGridY(event.getY()),
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
        playerSpawns.forEach(player -> player.render());
      }
    }.start();
  }

  private ArrayList<Line> redrawGrid() {
    // sets 10x10 grid based on scene size
    int sceneX = stageSizeX;
    int sceneY = stageSizeY;
    int gridX = gridSizeX;
    int gridY = gridSizeY;

    ArrayList<Line> gridlines = new ArrayList<Line>();
    if (snapToGrid) {
      for (int i = 0; i < gridX; i++) {
        int xPos = (sceneX / gridX) * i;
        Line line = new Line(xPos, 0, xPos, stageSizeY);
        gridlines.add(line);
      }
      for (int i = 0; i < gridY; i++) {
        int yPos = (sceneY / gridY) * i;
        Line line = new Line(0, yPos, stageSizeX, yPos);
        gridlines.add(line);
      }
    }

    return gridlines;
  }

  private void addButtons(Group root) {
    cb.setItems(
        FXCollections.observableArrayList(
            "ExampleObject",
            "Player Spawn Point",
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
            MapLoader.saveMap(gameObjects, mapDataObject, "menu.map");
          }
        });
    btnSave.setLayoutX(200);
    btnSave.setLayoutY(10);

    Button btnToggleGrid = new Button();
    btnToggleGrid.setText("Toggle Snap to Grid");
    btnToggleGrid.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            snapToGrid = !snapToGrid;
            String append;
            if (snapToGrid) {
              append = "ON";
            } else {
              append = "OFF";
            }
            btnToggleGrid.setText("Toggle Snap to Grid: " + append);
          }
        });
    btnToggleGrid.setLayoutX(300);
    btnToggleGrid.setLayoutY(10);

    ArrayList<Line> gridlines = redrawGrid();
    for (Line line : gridlines) {
      root.getChildren().add(line);
    } // todo remove

    root.getChildren().add(cb);
    root.getChildren().add(btnSave);
    root.getChildren().add(btnToggleGrid);
  }

  private void initialiseNewMap() {
    gameObjects = new ArrayList<>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.IN_GAME);
  }

  private double getGridX(double eventX) {
    if (snapToGrid) {
      return eventX - (eventX % gridSizePX);
    } else {
      return eventX;
    }
  }

  private double getGridY(double eventY) {
    if (snapToGrid) {
      return eventY - (eventY % gridSizePX);
    } else {
      return eventY;
    }
  }
}
