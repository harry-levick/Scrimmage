package client.handlers.accountHandler;

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

public class Achievement {

  private String name, description;
  private boolean status;

  public Achievement(String name, String description) {
    this.name = name;
    this.description = description;
  }


  public Image getImage() {
    return new Image(Path.convert("images/Achivements/golden_trophy_" + (status ? "100.png" : "greyed.png")));
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
    Label descriptionLabel = new Label(description);
    descriptionLabel.setStyle("-fx-font-size:15px; -fx-text-fill: white;");
    holder.getChildren().add(descriptionLabel);

    return holder;
  }

  public String getName() {
    return name;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
