package shared.gameObjects.menu;

import client.main.Client;
import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class LabelObject extends GameObject{

  private String label;
  private transient Text text;

  public LabelObject(
    double x, double y, String label, ObjectType id,
        UUID objectUUID) {
      super(x, y, 0, 0, id, objectUUID);
      this.label = label;
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);

    text = new Text(label);
    text.setLayoutX(getX());
    text.setLayoutY(getY());
    text.setFont(Client.settings.getFont(32));
    root.getChildren().add(text);
    text.setFill(Color.rgb(42, 177, 234));

  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().remove(text);
  }
}
