package shared.gameObjects.UI;

import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javax.swing.JProgressBar;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public class Health extends GameObject {

  protected transient ProgressBar bar;

  public Health(double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/ui/ui_hp.png");
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
  }

  @Override
  public String getState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void render() {
    super.render();
    imageView.relocate(getX(), getY());
  }
}
