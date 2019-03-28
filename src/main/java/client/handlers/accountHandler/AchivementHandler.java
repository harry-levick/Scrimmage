package client.handlers.accountHandler;

import client.main.Settings;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class AchivementHandler extends GameObject {

  private LinkedHashMap<String, Achievement> achivements;
  private ArrayList<GridPane> achivementPane;
  private KonamiCodeHandler handler;

  public AchivementHandler(UUID uuid) {
    super(0, 0, 1, 1, ObjectType.Button, uuid);
    achivements = new LinkedHashMap<>();
  }

  @Override
  public void update() {
    super.update();
  }

  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    handler = new KonamiCodeHandler();
    achivementPane = new ArrayList<>();
    loadAchivements();
    int pages = 0;
    int x = 0;
    int y = 0;
    int count = 0;
    GridPane page = new GridPane();
    achivementPane.add(page);
    for (Map.Entry<String, Achievement> entry : achivements.entrySet()) {
      count++;
      page.add(entry.getValue().getPane(), x, y, 1, 1);
      // TODO add gold particle if true
      // if(entry.getValue().isStatus()) //newPartcleEmitter
      x += 1;
      if (x >= 6) {
        y += 1;
        x = 0;
      }
      if (y >= 3 || count == achivements.size()) {
        y = 0;
        x = 0;
        pages += 1;
        page.setAlignment(Pos.CENTER);
        page.setHgap(150);
        page.setVgap(30);
        page = new GridPane();
        achivementPane.add(page);
      }
    }
    // Rectangle rec = new Rectangle(0, 0, settings.getWindowWidth(), settings.getWindowHeight());
    // root.getChildren().add(rec);
    root.getChildren().add(achivementPane.get(0));
    achivementPane.get(0).setTranslateX(100);
    achivementPane.get(0).setTranslateY(100);
  }

  public void loadAchivements() {
    achivements.clear();
    boolean[] status = settings.getData().getAchievements();
    try {
      Scanner fileScanner =
          new Scanner(new File(settings.getResourcesPath() + File.separator + "trophy.txt"));
      while (fileScanner.hasNextLine()) {
        String[] line = fileScanner.nextLine().split(",");
        Achievement achievement =
            new Achievement(
                Integer.parseInt(line[0]), line[1], line[2], status[Integer.parseInt(line[0])]);
        achivements.put(line[0], achievement);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().removeAll(achivementPane);
  }

  public static class KonamiCodeHandler implements EventHandler<KeyEvent> {

    int index;

    @Override
    public void handle(KeyEvent event) {
      System.out.println("Index: " + index + " Code: " + event.getCode());
      if (event.getEventType() == KeyEvent.KEY_PRESSED) {
        switch (index) {
          case 0:
          case 1:
            if (event.getCode() == KeyCode.UP) {
              index++;
            } else {
              index = 0;
            }
            break;
          case 2:
          case 3:
            if (event.getCode() == KeyCode.DOWN) {
              index++;
            } else {
              index = 0;
            }
            break;
          case 4:
          case 6:
            if (event.getCode() == KeyCode.LEFT) {
              index++;
            } else {
              index = 0;
            }
            break;
          case 5:
          case 7:
            if (event.getCode() == KeyCode.RIGHT) {
              index++;
            } else {
              index = 0;
            }
            break;
          case 8:
            if (event.getCode() == KeyCode.A) {
              index++;
            }
            break;
          case 9:
            if (event.getCode() == KeyCode.B) {
              System.out.println("KonamiCode");
            }
            break;
        }
      }
    }
  }
}
