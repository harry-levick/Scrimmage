package shared.gameObjects.players.Limbs;

import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;

public class Arm extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Arm(Boolean isLeft, Player parent, LevelHandler levelHandler) {
    super(13, 62, 53, 62, 17, 33, ObjectID.Player, isLeft, parent, 0, 0, levelHandler);
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
  public void addChild(GameObject child) {
    children.add(child);
    levelHandler.addGameObject(child);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/arm.png");
  }
}