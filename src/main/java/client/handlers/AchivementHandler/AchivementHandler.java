package client.handlers.AchivementHandler;

import client.main.Settings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class AchivementHandler {

  public static HashMap<String, Achivement> achivements = new HashMap<>();
  private Settings settings;

  public AchivementHandler(Settings settings) {
    this.settings = settings;
  }

  public void showAchivements(Group root) {
    ArrayList<GridPane> achivementPane = new ArrayList<>();
    loadAchivements();
    int pages = 0;
    int x = 0;
    int y = 0;
    int count = 0;
    GridPane page = new GridPane();
    for (Map.Entry<String, Achivement> entry : achivements.entrySet()) {
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
        achivementPane.add(page);
        page = new GridPane();
      }

    }
    //Rectangle rec = new Rectangle(0, 0, settings.getWindowWidth(), settings.getWindowHeight());
    //root.getChildren().add(rec);
    ImageView background = new ImageView(new Image(("images/backgrounds/background1.png")));
    background.setFitHeight(1080);
    background.setFitWidth(1920);
    root.getChildren().add(background);
    root.getChildren().add(achivementPane.get(0));
    achivementPane.get(0).setTranslateX(150);
    achivementPane.get(0).setTranslateY(150);
  }


  public void loadAchivements() {
    achivements.clear();
    try {
      Scanner fileScanner = new Scanner(new File(settings.getAchivementPath()));
      while (fileScanner.hasNextLine()) {
        String[] line = fileScanner.nextLine().split(",");
        Achivement achivement = new Achivement(line[0], Integer.parseInt(line[1]),
            Integer.parseInt(line[2]));
        achivements.put(line[0], achivement);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void saveAchivements() {
    BufferedWriter output = null;
    try {
      output = new BufferedWriter(new FileWriter(new File(settings.getAchivementPath())));
      for (Map.Entry<String, Achivement> entry : achivements.entrySet()) {
        Achivement achivement = entry.getValue();
        try {
          output.write(
              achivement.getName() + "," + achivement.getProgress() + "," + achivement.getTotal());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
