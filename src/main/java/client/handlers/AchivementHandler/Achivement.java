package client.handlers.AchivementHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.scene.image.Image;
import shared.util.Path;

public class Achivement {

  private String name;
  private int progress, total;

  public Achivement(String name, int progress, int total) {
    this.name = name;
    this.progress = progress;
    this.total = total;
  }

  public int getPercentage() {
    return (progress / total) * 100;
  }

  public Image getImage() {
    List list = new ArrayList();
    int percentage = getPercentage();
    list.add(0);
    list.add(25);
    list.add(50);
    list.add(75);
    list.add(100);

    int a = 0;
    try {
      a = (int) list.stream().min(Comparator.comparingInt(i -> Math.abs((int) i - percentage)))
          .orElseThrow(() -> new NoSuchElementException("No value present"));
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }

    return new Image(Path.convert("images/Achivements/golden_trophy_" + a + ".png"));
  }

}
