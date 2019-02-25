package shared.gameObjects.players.Limbs;

import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Limb;

public class Hand extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Hand(Boolean isLeft, GameObject parent) {
    //17 15
    super(-3, 20, 3, 20, 17, 15, ObjectID.Player, isLeft, parent);
  }


  @Override
  public void initialise(Group root) {
    super.initialise(root);
    if (isLeft) {
      imageView.setRotate(11);
    } else {
      imageView.setRotate(-15);
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/hand.png");
  }
}
