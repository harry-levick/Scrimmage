package shared.gameObjects.players.Limbs;

import static java.lang.Math.PI;

import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.util.maths.Vector2;

public class Arm extends Limb {

  private Rotate rotate;
  private boolean right;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public Arm(double x, double y, ObjectID id,
      UUID objectUUID, Player player, boolean right) {
    super(x, y, 17, 33, id, objectUUID);
    rotate = new Rotate();
    this.parent = player;
    this.right = right;
  }

  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/arm.png");
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    if (right) {
      imageView.setScaleX(-1);
    }
  }

  @Override
  public void update() {
    super.update();
    //Rotate
    imageView.getTransforms().remove(rotate);
    rotate.setPivotX(imageView.getX() + 7);
    rotate.setPivotY(imageView.getY() + 5);
    Player parentPlayer = (Player) parent;
    Vector2 mouseV = new Vector2((float) parentPlayer.mouseX, (float) parentPlayer.mouseY);
    Vector2 gripV = new Vector2((float) this.getX(), (float) this.getY());
    rotate.setAngle((mouseV.sub(gripV).angle()) * 180 / PI);
    imageView.getTransforms().add(rotate);

    if (!right) {
      setX(parent.getX() + 53);
      setY(parent.getY() + 65);
    } else {
      setX(parent.getX() + 13);
      setY(parent.getY() + 65);
    }
  }
}
