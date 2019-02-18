package server;

import client.main.Settings;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class Server extends Application {

  public static LevelHandler levelHandler;

  private final float timeStep = 0.0166f;
  private Map currentMap;
  private Settings settings;


  public static void main(String args[]) {
    launch(args);
  }

  public void init() {
    settings = new Settings();
    levelHandler = new LevelHandler(settings, null, null, null, false);
    levelHandler.changeMap(
        new Map("Lobby", Path.convert("src/main/resources/menus/lobby.map"), GameState.Lobby));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    new AnimationTimer() {

      @Override
      public void handle(long now) {
        processInputs();
        sendWorldState();
      }
    }.start();

  }

  public void processInputs() {

  }

  public void sendWorldState() {

  }
}
