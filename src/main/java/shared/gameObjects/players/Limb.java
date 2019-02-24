package shared.gameObjects.players;

import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public abstract class Limb extends GameObject {

  protected boolean isLeft;

  private double xLeft;
  private double yLeft;
  private double xRight;
  private double yRight;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param id Unique Identifier of every game object
   */
  public Limb(double xLeft, double yLeft, double xRight, double yRight, double sizeX, double sizeY,
      ObjectID id, Boolean isLeft, Player parent) {
    super(0, 0, sizeX, sizeY, id, UUID.randomUUID());
    this.isLeft = isLeft;
    this.xLeft = xLeft;
    this.yLeft = yLeft;
    this.xRight = xRight;
    this.yRight = yRight;
    this.parent = parent;
  }

  public abstract void initialiseAnimation();

  public void setRelativePosition() {
    if (isLeft) {
      setX(parent.getX() + xLeft);
      setY(parent.getY() + yLeft);
    } else {
      setX(parent.getX() + xRight);
      setY(parent.getY() + yRight);
    }
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    if (isLeft) {
      imageView.setScaleX(-1);
    }
  }

  @Override
  public void update() {
    super.update();
    setRelativePosition();
  }

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
  }
}

