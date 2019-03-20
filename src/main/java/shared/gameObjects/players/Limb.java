package shared.gameObjects.players;

import client.main.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.image.ImageView;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;


public abstract class Limb extends GameObject {

  /**
   * The X-coordinate pivot for rotating the limb
   */
  protected final double pivotX;
  /**
   * The Y-coordinate pivot for rotating the Limb
   */
  protected final double pivotY;
  //TODO idk what this does
  protected boolean isLeft;
  /**
   * JavaFX Rotation object used for rotating the limb around a pivot
   */
  protected transient Rotate rotate;
  /**
   * Boolean to determine if the limb is currently attached to an object
   */
  protected boolean limbAttached;
  //TODO idk what these does
  protected boolean lastAttachedCheck;
  protected Behaviour behaviour;
  protected Behaviour lastBehaviour;
  protected int action;
  protected HashMap<Behaviour, ArrayList<Integer>> actions;

  protected double xLeft;
  protected double yLeft;
  protected double xRight;
  protected double yRight;
  protected Player player;
  
  protected Rigidbody rb;
  protected BoxCollider bc;

  protected transient LevelHandler levelHandler;

  protected int resetOffsetX = 0;
  
  
  
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
    imageView.setX(imageView.getX()+resetOffsetX);
    resetOffsetX = 0;
    lastAttachedCheck = limbAttached;
  }

  public void reset() {
    removeRender();
  }
  
  private void getBehaviour() {
    this.behaviour = this.player.behaviour;

  }

  protected abstract void rotateAnimate();
  
  protected void flipImageView(ImageView iv, String direction) {
    if(direction.equals("WALK_LEFT")) {
      iv.setScaleX(-1);
    }
    else if(direction.equals("WALK_RIGHT")){
      iv.setScaleX(1);
    }
  }
  
  @Override
  public void render() {
    super.render();
    
    //Do all the rotations here.
    rotateAnimate();
    
    // Flip the imageView depending on the direciton of travel 
    flipImageView(imageView,this.behaviour.toString());
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

