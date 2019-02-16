package levelEditor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import levelEditor.LevelEditor.OBJECT_TYPES;
import shared.gameObjects.ExampleFloorObject;
import shared.gameObjects.ExampleWallObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.background.Background;
import shared.gameObjects.menu.main.ButtonLeveleditor;
import shared.gameObjects.menu.main.ButtonMultiplayer;
import shared.gameObjects.menu.main.ButtonSettings;
import shared.gameObjects.menu.main.ButtonSingleplayer;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Handgun;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.MapLoader;
import shared.util.maths.Vector2;

public class LevelEditor extends Application {

  private ArrayList<GameObject> gameObjects;
  private ArrayList<Player> playerSpawns = new ArrayList<Player>();
  private MapDataObject mapDataObject;
  private boolean snapToGrid = true;

  private int spawnPointLimit = 4; //todo autofetch

  private int stageSizeX = 1920; //todo autofetch
  private int stageSizeY = 1080;
  private int gridSizePX = 40;
  private int gridSizeX = stageSizeX / gridSizePX; //40 px blocks
  private int gridSizeY = stageSizeY / gridSizePX;

  private LinkedHashMap<OBJECT_TYPES, GameObjectTuple> objectMap = new LinkedHashMap<>();
  private OBJECT_TYPES objectTypeSelected = OBJECT_TYPES.FLOOR; // default

  /**
   * ADDING NEW OBJECTS TO THE MAP CREATOR: 1. add a new object name in the enum OBJECT_TYPES 2. in
   * the constructor add the enum mapped to a GameObjectTuple(label in the choicebox, grid x width,
   * grid y height) 3. in scenePrimaryClick, add a new case for the new enum and set temp to be an
   * instance of the new GameObject. Must break; the case. 4. debug
   */
  public LevelEditor() {
    objectMap.put(OBJECT_TYPES.FLOOR, new GameObjectTuple("Floor", 5, 2));
    objectMap.put(OBJECT_TYPES.WALL, new GameObjectTuple("Wall", 2, 2));
    objectMap.put(OBJECT_TYPES.PLAYER, new GameObjectTuple("Player Spawn", 2, 3));
    objectMap.put(OBJECT_TYPES.BACKGROUND, new GameObjectTuple("Background", 0, 0));
    objectMap.put(OBJECT_TYPES.BACKGROUND1, new GameObjectTuple("Background 2", 0, 0));
    objectMap.put(OBJECT_TYPES.BTN_SP, new GameObjectTuple("Singeplayer Button", 6, 2));
    objectMap.put(OBJECT_TYPES.BTN_MP, new GameObjectTuple("Multiplayer Button", 6, 2));
    objectMap.put(OBJECT_TYPES.BTN_ST, new GameObjectTuple("Settings Button", 6, 2));
    objectMap.put(OBJECT_TYPES.BTN_LE, new GameObjectTuple("Level Editor Button", 6, 2));
    objectMap.put(OBJECT_TYPES.WPN_HG, new GameObjectTuple("Handgun", 2, 2));
  }

  private void scenePrimaryClick(Stage primaryStage, Group root, Group objects, Group background, MouseEvent event) {
    if (!isInObject(event.getX(), event.getY(), objectMap.get(objectTypeSelected).getX(),
        objectMap.get(objectTypeSelected).getY())) {
      GameObject temp = null;
      UUID uuid = UUID.randomUUID();
      switch (objectTypeSelected) {
        case FLOOR:
        default:
          temp =
              new ExampleFloorObject(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  ObjectID.Bot,
                  uuid);
          break;

        case WALL:
          temp =
              new ExampleWallObject(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  ObjectID.Bot,
                  uuid);
          break;

        case PLAYER:
          if (mapDataObject.getSpawnPoints().size() < spawnPointLimit) {
            temp =
                new Player(
                    getGridX(event.getX()),
                    getGridY(event.getY()),
                    getScaledSize(objectMap.get(objectTypeSelected).getX()),
                    getScaledSize(objectMap.get(objectTypeSelected).getY()),
                    uuid);
            mapDataObject.addSpawnPoint(getGridX(event.getX()), getGridY(event.getY()));
          } else {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            Text text =
                new Text(
                    "\n\tWarning: You cannot create more than "
                        + spawnPointLimit
                        + " spawn points.");
            dialogVbox.getChildren().add(text);
            Scene dialogScene = new Scene(dialogVbox, 450, 60);
            dialog.setScene(dialogScene);
            dialog.show();
          }
          break;

        case BACKGROUND:
          temp = new Background("images/backgrounds/background1.png", ObjectID.Background, uuid);
          mapDataObject.setBackground((Background) temp);
          break;

        case BACKGROUND1:
          temp = new Background("images/backgrounds/base.png", ObjectID.Background, uuid);
          mapDataObject.setBackground((Background) temp);
          break;

        case BTN_SP:
          temp =
              new ButtonSingleplayer(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  ObjectID.Bot,
                  uuid);
          break;

        case BTN_MP:
          temp =
              new ButtonMultiplayer(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  ObjectID.Bot,
                  uuid);
          break;

        case BTN_ST:
          temp =
              new ButtonSettings(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  ObjectID.Bot,
                  uuid);
          break;

        case BTN_LE:
          temp =
              new ButtonLeveleditor(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  ObjectID.Bot,
                  uuid);
          break;

        case WPN_HG:
          temp =
              new Handgun(
                  getGridX(event.getX()),
                  getGridY(event.getY()),
                  getScaledSize(objectMap.get(objectTypeSelected).getX()),
                  getScaledSize(objectMap.get(objectTypeSelected).getY()),
                  "Handgun",
                  uuid);
          break;
      }

      if (temp != null) {
        if (temp.getId() == ObjectID.Background) {
          temp.initialise(background);
        } else {
          temp.initialise(objects);
        }
        if (objectTypeSelected == OBJECT_TYPES.PLAYER && temp.getId() != ObjectID.Background) {
          playerSpawns.add((Player) temp);
        } else if (temp.getId() != ObjectID.Background) {
          gameObjects.add(temp);
        }
      }
    }
  }

  private void addButtons(Group root) {
    ChoiceBox cb = new ChoiceBox();
    cb.setConverter(new GameObjectTupleConverter(objectMap));
    cb.setItems(FXCollections.observableArrayList(objectMap.values()));
    cb.setLayoutX(10);
    cb.setLayoutY(10);
    cb.setTooltip(new Tooltip("Select item to place on the map"));
    cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number number,
          Number number2) {
        GameObjectTupleConverter con = new GameObjectTupleConverter(objectMap);
        for (Entry<OBJECT_TYPES, GameObjectTuple> e : objectMap.entrySet()) {
          if (con.toString((GameObjectTuple) cb.getItems().get((Integer) number2))
              .equals(con.toString(e.getValue()))) {
            objectTypeSelected = e.getKey();
          }
        }
      }
    });
    cb.setValue(cb.getItems().get(0));

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

    root.getChildren().add(cb);
    root.getChildren().add(btnSave);
    root.getChildren().add(btnToggleGrid);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Level Editor");
    Group ui = new Group();
    Group objects = new Group();
    Group background = new Group();
    Group grid = new Group();
    Group root = new Group();

    root.getChildren().add(background);
    root.getChildren().add(grid);
    root.getChildren().add(objects);
    root.getChildren().add(ui);

    initialiseNewMap();

    // Example of loading map
    // gameObjects = MapLoader.loadMap("menus.map");
    //gameObjects.forEach(gameObject -> gameObject.initialise(root));

    addButtons(ui);

    ArrayList<Line> gridlines = redrawGrid();
    for (Line line : gridlines) {
      grid.getChildren().add(line);
    } // todo remove

    Scene scene = new Scene(root, stageSizeX, stageSizeY);
    scene.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
              scenePrimaryClick(primaryStage, root, objects, background, event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
              sceneSecondaryClick(primaryStage, objects, event);
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
        if (mapDataObject.getBackground() != null) {
          mapDataObject.getBackground().render();
        }
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

  protected enum OBJECT_TYPES {
    FLOOR, WALL, PLAYER, BTN_SP, BTN_MP, BTN_ST, BTN_LE, WPN_HG, BACKGROUND, BACKGROUND1
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

  private boolean isInObject(double x, double y, int newObjX, int newObjY) {
    boolean conflict = false;
    for (GameObject object : gameObjects) {
      double ulX = object.getX();
      double ulY = object.getY();
      double lrX = ulX + object.getTransform().getSize().getX();
      double lrY = ulY + object.getTransform().getSize().getY();
      double lrMX = getGridX(x) + getScaledSize(newObjX);
      double lrMY = getGridY(y) + getScaledSize(newObjY);
      if (((x >= ulX) && (y >= ulY) && (x <= lrX) && (y <= lrY)) // ul inside
          || ((lrMX > ulX) && (lrMY > ulY) && (lrMX <= lrX) && (lrMY <= lrY)) //lr inside
          || ((lrMX > ulX) && (y > ulY) && (lrMX <= lrX) && (y <= lrY)) //ur inside
          || ((x > ulX) && (lrMY > ulY) && (x <= lrX) && lrMY <= lrY)) { //ll inside
        conflict = true;
      }
    }
    for (Player object : playerSpawns) {
      double ulX = object.getX();
      double ulY = object.getY();
      double lrX = ulX + object.getTransform().getSize().getX();
      double lrY = ulY + object.getTransform().getSize().getY();
      double lrMX = getGridX(x) + getScaledSize(newObjX);
      double lrMY = getGridY(y) + getScaledSize(newObjY);
      if (((x >= ulX) && (y >= ulY) && (x <= lrX) && (y <= lrY)) // ul inside
          || ((lrMX > ulX) && (lrMY > ulY) && (lrMX <= lrX) && (lrMY <= lrY)) //lr inside
          || ((lrMX > ulX) && (y > ulY) && (lrMX <= lrX) && (y <= lrY)) //ur inside
          || ((x > ulX) && (lrMY > ulY) && (x <= lrX) && lrMY <= lrY)) { //ll inside
        conflict = true;
      }
    }
    return conflict;
  }

  private void sceneSecondaryClick(Stage primaryStage, Group root, MouseEvent event) {
    ArrayList<GameObject> removeList = gameObjects;
    ArrayList<Player> removeSpawn = playerSpawns;
    double x = event.getX();
    double y = event.getY();
    for (GameObject object : removeList) {
      double ulX = object.getX();
      double ulY = object.getY();
      double lrX = ulX + object.getTransform().getSize().getX();
      double lrY = ulY + object.getTransform().getSize().getY();
      if ((x >= ulX) && (y >= ulY) && (x <= lrX) && (y <= lrY)) {
        root.getChildren().remove(event.getTarget());
        object.destroy();
        gameObjects.remove(object);  //todo find alternative non breaking way of removing
      }
    }

    for (Player object : playerSpawns) {
      double ulX = object.getX();
      double ulY = object.getY();
      double lrX = ulX + object.getTransform().getSize().getX();
      double lrY = ulY + object.getTransform().getSize().getY();
      if ((x >= ulX) && (y >= ulY) && (x <= lrX) && (y <= lrY)) {
        root.getChildren().remove(event.getTarget());
        Vector2 target = object.getTransform().getPos();
        ArrayList<Vector2> newList = new ArrayList<Vector2>();
        for (Vector2 spawnpoint : mapDataObject.getSpawnPoints()) {
          if (!spawnpoint.equals(target)) {
            newList.add(spawnpoint);
          }
        }
        mapDataObject.setSpawnPoints(newList);
        object.destroy();
        gameObjects.remove(object); //todo find alternative non breaking way of removing
      }
    }
  }

  private double getScaledSize(int gridSquaresCovered) {
    return gridSizePX * gridSquaresCovered;
  }
}

class GameObjectTuple {

  private String label;
  private int x;
  private int y;

  protected GameObjectTuple(String label, int x, int y) {
    this.label = label;
    this.x = x;
    this.y = y;
  }

  protected String getLabel() {
    return label;
  }

  protected int getX() {
    return x;
  }

  protected int getY() {
    return y;
  }
}

class GameObjectTupleConverter extends StringConverter<GameObjectTuple> {

  private LinkedHashMap<OBJECT_TYPES, GameObjectTuple> hashHap;

  protected GameObjectTupleConverter(LinkedHashMap<OBJECT_TYPES, GameObjectTuple> objectHash) {
    hashHap = objectHash;
  }

  public GameObjectTuple fromString(String string) {
    // convert from a string to a myClass instance
    GameObjectTuple tuple = new GameObjectTuple("null-error", 1, 1);
    for (GameObjectTuple listTuple : hashHap.values()) {
      if (listTuple.getLabel().equals(string)) {
        tuple = listTuple;
      }
    }
    return tuple;
  }

  public String toString(GameObjectTuple tuple) {
    // convert a myClass instance to the text displayed in the choice box
    return tuple.getLabel();
  }
}
