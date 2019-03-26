package client.handlers.accountHandler;

import client.main.Settings;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class AchivementHandler extends GameObject {

  private HashMap<String, Achievement> achivements;

  public AchivementHandler(UUID uuid) {
    super(0, 0, 1, 1, ObjectType.Button, uuid);
    achivements = new HashMap<>();
  }

  public void initialise(Group root , Settings settings) {
    super.initialise(root, settings);
    ArrayList<GridPane> achivementPane = new ArrayList<>();
    loadAchivements();
    int pages = 0;
    int x = 0;
    int y = 0;
    int count = 0;
    GridPane page = new GridPane();
    achivementPane.add(page);
    for (Map.Entry<String, Achievement> entry : achivements.entrySet()) {
      count++;
      page.add(entry.getValue().getPane(), y, x, 1, 1);
      x += 1;
      if (x >= 3) {
        y += 1;
        x = 0;
      }
      if (y >= 5 || count == achivements.size()) {
        y = 0;
        x = 0;
        pages += 1;
        page.setAlignment(Pos.CENTER);
        page.setHgap(50);
        page.setVgap(30);
        page = new GridPane();
        achivementPane.add(page);
      }

    }
    //Rectangle rec = new Rectangle(0, 0, settings.getWindowWidth(), settings.getWindowHeight());
    //root.getChildren().add(rec);
    root.getChildren().add(achivementPane.get(0));
    achivementPane.get(0).setTranslateX(150);
    achivementPane.get(0).setTranslateY(150);
  }


  public void loadAchivements() {
    achivements.clear();
    try {
      Scanner fileScanner = new Scanner(new File(settings.getResourcesPath() + File.separator + "trophy.txt"));
      while (fileScanner.hasNextLine()) {
        String[] line = fileScanner.nextLine().split(",");
        Achievement achievement = new Achievement(line[0], line[1]);
        achivements.put(line[0], achievement);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}
