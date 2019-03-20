package shared.gameObjects.players.Limbs;

import client.main.Settings;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import shared.util.maths.Vector2;

public class Arm extends Limb {

  private static double AIM_ANGLE_MAX = 110;
  private static double PI = 3.141592654;
  private static double DEFAULT_ANGLE_LEFT = 6;
  private static double DEFAULT_ANGLE_RIGHT = -9;
  private double angleRadian;
  private Limb hand;
  private Player playerParent;
  private Rotate rotate;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Arm(Boolean isLeft, Player parent, LevelHandler levelHandler) {
    super(13, 62, 53, 62, 17, 33, ObjectType.Limb, isLeft, parent, 0, 0, levelHandler);
    this.playerParent = parent;
    rotate = new Rotate();
    rotate.setPivotX(10);
    rotate.setPivotY(10);
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    if (isLeft) {
      //imageView.setRotate(6);
    } else {
      //imageView.setRotate(-9);
    }
  }

  @Override
  public void render() {
    super.render();
    if (this.hand == null) {
      return;
    }

    double xHand = this.getX();
    double yHand = this.getY();
    double xMouse = this.playerParent.mouseX;
    double yMouse = this.playerParent.mouseY;

    Vector2 v = new Vector2(xMouse - xHand, yMouse - yHand);
    float angle = v.angleBetween(Vector2.Zero()) * 180 / 3.141592654f;
    imageView.getTransforms().clear();

    if (isLeft) {
      if (playerParent.isPointingLeft()) {
        rotate.setAngle(270 - angle);
      } else {
        rotate.setAngle(DEFAULT_ANGLE_LEFT);
      }
    } else {
      if (!(playerParent.isPointingLeft())) {
        rotate.setAngle(270 + angle);
      } else {
        rotate.setAngle(DEFAULT_ANGLE_RIGHT);
      }
    }

    imageView.getTransforms().add(rotate);

  }

  @Override
  public void addChild(GameObject child) {
    children.add(child);
    levelHandler.addGameObject(child);
    if (hand == null && child instanceof Hand) {
      this.hand = (Hand) child;
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/arm.png");
  }
}
