package shared.gameObjects.players.Limbs;

import static java.lang.Math.PI;

import javafx.scene.transform.Rotate;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.util.maths.Vector2;

public class Arm extends Limb {

  private Rotate rotate;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   */
  public Arm(boolean isLeft, Player parent) {
    super(0, 0, 0, 0, 17, 33, ObjectID.Player, isLeft, Player parent);
    rotate = new Rotate();
  }

  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/arm.png");
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

  }


}
