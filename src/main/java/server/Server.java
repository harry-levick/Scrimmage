package server;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Server extends Application {

  public static void main(String args[]) {
    launch(args);
  }

  public void init() {

  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    new AnimationTimer() {

      @Override
      public void handle(long now) {

      }
    }.start();

  }
}
