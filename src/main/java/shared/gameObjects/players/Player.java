package shared.gameObjects.players;

import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.Destructable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.components.behaviours.ObjectShake;
import shared.gameObjects.players.Limbs.Arm;
import shared.gameObjects.players.Limbs.Body;
import shared.gameObjects.players.Limbs.Hand;
import shared.gameObjects.players.Limbs.Head;
import shared.gameObjects.players.Limbs.Leg;
import shared.gameObjects.weapons.MachineGun;
import shared.gameObjects.weapons.Punch;
import shared.gameObjects.weapons.Weapon;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class Player extends GameObject implements Destructable {

  /**
   * The speed of the player in pixels per update frame
   */
  protected final float speed = 9;
  /**
   * The jump force of the player in Newtons
   */
  protected final float jumpForce = -300;
  /**
   * Control Booleans determined by the Input Manager
   */
  public boolean leftKey, rightKey, jumpKey, click;
  //Testing
  public boolean deattach;
  /**
   * Boolean to determine when the "Throw Weapon" key is pressed down
   */
  public boolean throwHoldingKey;
  /**
   * Saves the current position of the cursor (X) and (Y)
   */
  public double mouseX, mouseY;
  /**
   * The score of the player used to determine win conditions
   */
  public int score;
  //TODO idk what this does
  protected Behaviour behaviour;
  /**
   * Boolean to determine if a player has jumped or not
   */
  protected boolean jumped;
  /**
   * Boolean to determine if a player is on the ground or in the air
   */
  protected boolean grounded;
  /**
   * Boolean to determine if the player is facing left
   */
  protected boolean facingLeft;
  /**
   * Boolean to determine if the player is facing right
   */
  protected boolean facingRight;
  //TODO idk what this does
  protected boolean aimLeft;
  /**
   * The current health of the player; is killed when reaches 0
   */
  protected int health;
  /**
   * The current weapon the player is using
   */
  protected Weapon holding;
  /**
   * When not holding a weapon, it defaults to the "Punch" weapon
   */
  protected Weapon myPunch;
  /**
   * The Physics Rigidbody component attached to the player
   */
  protected Rigidbody rb;
  //TODO idk what this does
  protected double vx;
  private BoxCollider bc;

  // Limbs
  private Limb head;
  private Limb body;
  private Limb legLeft;
  private Limb legRight;
  private Limb armLeft;
  private Limb armRight;
  private Limb handLeft;
  private Limb handRight;


  //Networking
  private int lastInputCount;

  /**
   *
   * Constructs a player object in the scene
   * @param x The X-Coordinate of the object
   * @param y The Y-Coordinate of the object
   * @param playerUUID The uuid of the object
   */
  public Player(double x, double y, UUID playerUUID) {
    super(x, y, 80, 110, ObjectType.Player, playerUUID);
    this.lastInputCount = 0;
    this.score = 0;
    this.leftKey = false;
    this.rightKey = false;
    this.jumpKey = false;
    this.click = false;
    this.health = 100;
    this.behaviour = Behaviour.IDLE;
    this.bc = new BoxCollider(this, ColliderLayer.PLAYER, false);
    this.rb = new Rigidbody(RigidbodyType.DYNAMIC, 90, 11.67f, 0.2f,
        new MaterialProperty(0f, 0.1f, 0.05f), null, this);
    addComponent(bc);
    addComponent(rb);
  }

  // Initialise the animation
  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/player_idle.png");
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    addLimbs();

    myPunch = new Punch(getX(), getY(), "myPunch@Player", this, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(myPunch);
    this.holding = myPunch;
  }

  private void addLimbs() {
    legLeft = new Leg(true, this, settings.getLevelHandler());
    legRight = new Leg(false, this, settings.getLevelHandler());
    body = new Body(this, settings.getLevelHandler());
    head = new Head(this, settings.getLevelHandler());
    armLeft = new Arm(true, this, settings.getLevelHandler());
    armRight = new Arm(false, this, settings.getLevelHandler());
    handLeft = new Hand(true, armLeft, settings.getLevelHandler());
    handRight = new Hand(false, armRight, settings.getLevelHandler());
    addChild(legLeft);
    addChild(legRight);
    addChild(body);
    addChild(head);
    addChild(armLeft);
    addChild(armRight);
    armRight.addChild(handRight);
    armLeft.addChild(handLeft);
  }

  @Override
  public void update() {
    checkGrounded(); // Checks if the player is grounded
    badWeapon();
    if (deattach) {
      for (int i = 0; i < 6; i++) {
        Limb test = (Limb) children.get(i);
        test.detachLimb();
      }
    }
    super.update();
  }


  @Override
  public String getState() {
    return objectUUID + ";" + id + ";" + getX() + ";" + getY() + ";" + animation.getName() + ";"
        + health + ";"
        + lastInputCount;
  }

  @Override
  public void setState(String data, Boolean snap) {
    super.setState(data, snap);
    String[] unpackedData = data.split(";");
    //this.animation.switchAnimation(unpackedData[4]);
    this.health = Integer.parseInt(unpackedData[5]);
    this.lastInputCount = Integer.parseInt(unpackedData[6]);
  }

  private void checkGrounded() {
    grounded = rb.isGrounded();
  }

  /**
   * Applies the inputs at the beginning of the frame
   */
  public void applyInput() {
    if (rightKey) {
      rb.moveX(speed);
      behaviour = Behaviour.WALK_RIGHT;
      this.facingLeft = false;
      this.facingRight = true;
    }
    if (leftKey) {
      rb.moveX(speed * -1);
      behaviour = Behaviour.WALK_LEFT;
      this.facingRight = false;
      this.facingLeft = true;
    }

    if (!rightKey && !leftKey) {
      vx = 0;
      behaviour = Behaviour.IDLE;
    }
    if (jumpKey && !jumped && grounded) {
      rb.moveY(jumpForce, 0.33333f);
      jumped = true;
    }
    if (jumped) {
      behaviour = Behaviour.JUMP;
    }
    if (grounded) {
      jumped = false;
    }
    if (throwHoldingKey) {
      this.throwHolding();
    }
    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    }
    // setX(getX() + (vx * 0.0166));
  }

  /**
   * Check if the current holding weapon is valid or not
   *
   * @return False if the weapon is a good weapon, or there is no weapon
   */
  public boolean badWeapon() {
    if (this.holding == null) {
      return false;
    }
    if (this.holding.getAmmo() == 0) {
      this.holding.destroyWeapon();
      this.setHolding(myPunch);
      return true;
    }
    return false;
  }

  /**
   * Throws the weapon currently held with a velocity
   */
  public void throwHolding() {
    if (this.holding == null || this.holding instanceof Punch) {
      return;
    }
    Weapon w = this.holding;
    w.startThrowing();
  }

  @Override
  public void deductHp(int damage) {
    this.health -= damage;
    if (this.health <= 0) {
      // For testing
      transform.translate(new Vector2(0, -80));
      this.setActive(false);
      bc.setLayer(ColliderLayer.PARTICLE);
      transform.rotate(90);
      this.imageView.setOpacity(0.5);
    }
  }

  /**
   * Resets the player's values, a "respawn"
   */
  public void reset() {
    health = 100;
    if (this.active == false) {
      this.imageView.setRotate(0);
      this.imageView.setTranslateY(getY() - 70);
      this.setActive(true);
      this.bc.setLayer(ColliderLayer.PLAYER);
    }
    children.forEach(child -> {
      Limb limb = (Limb) child;
      limb.reset();
    });
    children.clear();
    addLimbs();
  }

  /**
   * Increases the player score by one unit
   */
  public void increaseScore() {
    score++;
  }

  /**
   * Increases the player score as per the game condition
   */
  public void increaseScore(int amount) {
    score += amount;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int hp) {
    this.health = hp;
  }

  public Weapon getHolding() {
    return holding==null? myPunch : holding;
  }

  public void setHolding(Weapon newHolding) {
    this.holding = newHolding;

    if (newHolding != null) {
      newHolding.setSettings(settings);
      aimLeft = false;
    }
  }

  public void usePunch() {
    this.setHolding(this.myPunch);
  }

  public int getScore() {
    return score;
  }

  /**
   * The back hand will be the main hand which holds the gun
   *
   * @return A 2 elements array, a[0] = X position of the hand, a[1] = Y position of the hand
   */
  public double[] getGunHandPos() {
    // TODO: remove this section and getHand(Left/Right)(X/Y) methods below
    /*
    if (jumped && facingLeft) {
      return new double[]{this.getHandLeftJumpX(), this.getHandLeftJumpY()};
    } else if (jumped && facingRight) {
      return new double[]{this.getHandRightJumpX(), this.getHandRightJumpY()};
    } else if (facingLeft) {
      return new double[]{this.handLeft.getX(), this.handLeft.getY()};
    } else if (facingRight) {
      return new double[]{this.handRight.getX(), this.handRight.getY()};
    }
    return new double[]{this.getHandRightX(), this.getHandRightY()};
    */
    if (isAimingLeft()) {
      return new double[]{this.handRight.getX(), this.handRight.getY()};
    } else {
      return new double[]{this.handLeft.getX(), this.handLeft.getY()};
    }
  }

  /**
   * The front facing hand will be the main hand which holds the melee
   *
   * @return A 2 elements array, a[0] = X position of the hand, a[1] = Y position of the hand
   */
  public double[] getMeleeHandPos() {
    if (isAimingLeft()) {
      return new double[]{this.handLeft.getX(), this.handLeft.getY()};
    } else {
      return new double[]{this.handRight.getX(), this.handRight.getY()};
    }
  }

  public boolean containsChild(GameObject child) {
    for (GameObject c : children) {
      if (c.getUUID() == child.getUUID()) {
        return true;
      }
    }
    return false;
  }

  public void setHandRightX(double pos) {
    this.handRight.setX(pos);
  }

  public void setHandRightY(double pos) {
    this.handRight.setY(pos);
  }

  public void setHandLeftX(double pos) {
    this.handLeft.setX(pos);
  }

  public void setHandLeftY(double pos) {
    this.handLeft.setY(pos);
  }

  public boolean getJumped() {
    return this.jumped;
  }

  public boolean getFacingLeft() {
    return this.facingLeft;
  }

  public void setFacingLeft(boolean b) {
    this.facingLeft = b;
    this.facingRight = !b;
  }

  public boolean getFacingRight() {
    return this.facingRight;
  }

  public void setFacingRight(boolean b) {
    this.facingLeft = !b;
    this.facingRight = b;
  }

  public boolean isAimingLeft() {
    return this.aimLeft;
  }

  public void setAimingLeft(boolean b) {
    this.aimLeft = b;
  }

  public boolean isGrounded() {
    return grounded;
  }

  public int getLastInputCount() {
    return lastInputCount;
  }

  public void setLastInputCount(int lastInputCount) {
    this.lastInputCount = lastInputCount;
  }
}
