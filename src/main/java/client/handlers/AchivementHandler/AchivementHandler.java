package client.handlers.AchivementHandler;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.GridPane;

public class AchivementHandler {

  public static HashMap<String, Achivement> achivements = new HashMap<>();
  private ArrayList<GridPane> achivementPane;

  public AchivementHandler() {
    achivementPane = new ArrayList<>();
  }

  public void showAchivements() {

  }

  public void hideAchivements() {

  }

  public void loadAchivements() {
    achivements.clear();

  }

}
