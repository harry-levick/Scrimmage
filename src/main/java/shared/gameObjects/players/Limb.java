package shared.gameObjects.players;

import client.main.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

public abstract class Limb extends GameObject {

  protected final double pivotX;
  protected final double pivotY;
  protected boolean isLeft;
  protected transient Rotate rotate;
  protected boolean limbAttached;
  protected boolean lastAttachedCheck;
  protected Behaviour behaviour;
  protected Behaviour lastBehaviour;
  protected int action;
  protected HashMap<Behaviour, ArrayList<Integer>> actions;

  protected double xLeft;
  protected double yLeft;
  protected double xRight;
  protected double yRight;
  private Player player;
  
  protected Rigidbody rb;
  protected BoxCollider bc;

  protected transient LevelHandler levelHandler;

  private int r= 0;
  
  
  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param id Unique Identifier of every game object
   */
  public Limb(double xLeft, double yLeft, double xRight, double yRight, double sizeX, double sizeY,
      ObjectType id, Boolean isLeft, GameObject parent, Player player, double pivotX, double pivotY,
      LevelHandler levelHandler) {
    super(0, 0, sizeX, sizeY, id, UUID.randomUUID());
    this.limbAttached = true;
    this.lastAttachedCheck = true;
    this.isLeft = isLeft;
    this.xLeft = xLeft;
    this.yLeft = yLeft;
    this.xRight = xRight;
    this.yRight = yRight;
    this.parent = parent;
    this.player = player;
    this.rotate = new Rotate();
    this.behaviour = Behaviour.IDLE;
    this.actions = new HashMap<>();
    this.lastBehaviour = Behaviour.IDLE;
    this.action = 0;
    this.pivotX = pivotX;
    this.pivotY = pivotY;
    this.levelHandler = levelHandler;

    //Physics
    bc = new BoxCollider(this, false);
    addComponent(bc);

    rb =
        new Rigidbody(
            RigidbodyType.DYNAMIC, 80, 8, 0.2f, new MaterialProperty(0.005f, 0.1f, 0.05f), null,
            this);
    rotate.setPivotX(pivotX);
    rotate.setPivotY(pivotY);
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
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    if (isLeft) {
      imageView.setScaleX(-1);
    }
  }

  public void addChild(GameObject child, boolean init) {
    if (init) {
      children.add(child);
    }
    levelHandler.addGameObject(child);
  }

  @Override
  public void update() {
    super.update();
    getBehaviour();
    if (limbAttached) {
      setRelativePosition();
      if (!lastAttachedCheck) {
        removeComponent(rb);
      }
    } else {
      if (lastAttachedCheck) {
        addComponent(rb);
      }
    }
    imageView.getTransforms().remove(rotate);
    lastAttachedCheck = limbAttached;
  }

  public void reset() {
    removeRender();
  }
  
  private void getBehaviour() {
    this.behaviour = this.player.behaviour;
  }

  protected abstract void rotateAnimate();
  
  @Override
  public void render() {
    super.render();
    
    //Do all the rotations here.
    rotateAnimate();
  }

  public boolean isLimbAttached() {
    return limbAttached;
  }

  public void detachLimb() {
    this.limbAttached = false;
  }

  public void reattachLimb() {
    this.limbAttached = true;
  }
}

