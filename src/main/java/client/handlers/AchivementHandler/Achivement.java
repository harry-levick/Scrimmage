package client.handlers.AchivementHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import shared.util.Path;

public class Achivement {

  private String name, description;
  private double progress, total;

  public Achivement(String name, int progress, int total) {
    this.name = name;
    this.progress = progress;
    this.total = total;
    this.description = "Probably should put something here";
  }


  public Image getImage() {
    List list = new ArrayList();
    double temp = progress / total;
    double percentage = temp * 100;
    list.add(0);
    list.add(25);
    list.add(50);
    list.add(75);
    list.add(100);

    int a = 0;
    try {
      a = (int) list.stream()
          .min(Comparator.comparingInt(i -> Math.abs((int) i - (int) Math.round(percentage))))
          .orElseThrow(() -> new NoSuchElementException("No value present"));
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }

    return new Image(Path.convert("images/Achivements/golden_trophy_" + a + ".png"));
  }

  public Pane getPane() {
    VBox holder = new VBox();
    holder.setAlignment(Pos.CENTER);

    //Name
    Label nameLabel = new Label(getName());
    nameLabel.setStyle("-fx-font-size:30px; -fx-text-fill: white;");
    holder.getChildren().add(nameLabel);

    StackPane stackPane = new StackPane();

    //Grey Icon
    ImageView iconGrey = new ImageView(
        new Image(Path.convert("images/Achivements/golden_trophy_greyed.png")));
    iconGrey.setFitWidth(120);
    iconGrey.setFitHeight(145);
    iconGrey.setOpacity(0.8);
    stackPane.getChildren().add(iconGrey);

    //Icon
    ImageView icon = new ImageView(getImage());
    icon.setFitWidth(120);
    icon.setFitHeight(145);
    stackPane.getChildren().add(icon);

    holder.getChildren().add(stackPane);


    //Progress
    Label progressLabel = new Label((int) progress + " / " + (int) total);
    progressLabel.setStyle("-fx-font-size:15px; -fx-text-fill: white;");
    holder.getChildren().add(progressLabel);

    //Progress
    Label descriptionLabel = new Label(description);
    descriptionLabel.setStyle("-fx-font-size:15px; -fx-text-fill: white;");
    holder.getChildren().add(descriptionLabel);

    return holder;
  }

  public String getName() {
    return name;
  }

  public double getProgress() {
    return progress;
  }

  public double getTotal() {
    return total;
  }
}
