package shared.gameObjects.players.Limbs;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

public class Head extends Limb {

  protected Rigidbody rb;
  private BoxCollider bc;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Head(Player parent) {
    super(0, 0, 17, 13, 48, 58, ObjectID.Player, false, parent);
    bc = new BoxCollider(this, false);
    addComponent(bc);
    rb =
        new Rigidbody(
            RigidbodyType.DYNAMIC, 80, 8, 0.2f, new MaterialProperty(0.005f, 0.1f, 0.05f), null,
            this);
    addComponent(rb);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/head.png");
  }
}
