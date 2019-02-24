package shared.gameObjects.players.Limbs;

import javafx.scene.Group;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

public class Arm extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   */
  public Arm(Boolean isLeft, Player parent) {
    super(13, 62, 53, 62, 17, 33, ObjectID.Player, isLeft, parent);
    bc = new BoxCollider(this, false);
    addComponent(bc);
    rb =
        new Rigidbody(
            RigidbodyType.DYNAMIC, 80, 8, 0.2f, new MaterialProperty(0.005f, 0.1f, 0.05f), null,
            this);
    addComponent(rb);
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    if (isLeft) {
      imageView.setRotate(6);
    } else {
      imageView.setRotate(-9);
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/arm.png");
  }
}
