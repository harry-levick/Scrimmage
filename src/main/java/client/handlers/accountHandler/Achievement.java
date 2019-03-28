package client.handlers.accountHandler;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import shared.util.Path;

public class Achievement {

  private String name, description;
  private boolean status;
  private int id;

  public Achievement(int id, String name, String description, boolean status) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.status = status;
  }

  public Image getImage() {
    return new Image(
        Path.convert("images/Achivements/golden_trophy_" + (status ? "100.png" : "greyed.png")));
  }

  public Pane getPane() {
    VBox holder = new VBox();
    holder.setAlignment(Pos.CENTER);

    // Name
    Label nameLabel = new Label(name);
    nameLabel.setStyle("-fx-font-size:20px; -fx-text-fill: white;");
    nameLabel.setMaxWidth(160);
    nameLabel.setWrapText(true);
    nameLabel.setAlignment(Pos.CENTER);
    holder.getChildren().add(nameLabel);

    StackPane stackPane = new StackPane();

    // Icon
    ImageView icon = new ImageView(getImage());
    icon.setFitWidth(120);
    icon.setFitHeight(145);
    stackPane.getChildren().add(icon);

    holder.getChildren().add(stackPane);

    // Progress
    Label descriptionLabel = new Label(description);
    descriptionLabel.setStyle("-fx-font-size:15px; -fx-text-fill: white;");
    descriptionLabel.setMaxWidth(160);
    descriptionLabel.setWrapText(true);
    descriptionLabel.setTextAlignment(TextAlignment.CENTER);
    holder.getChildren().add(descriptionLabel);

    return holder;
  }

  public String getName() {
    return name;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
