package shared.gameObjects.players;

import client.handlers.effectsHandler.Particle;
import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Limbs.Arm;
import shared.gameObjects.players.Limbs.Body;
import shared.gameObjects.players.Limbs.Hand;
import shared.gameObjects.players.Limbs.Head;
import shared.gameObjects.players.Limbs.Leg;
import shared.gameObjects.weapons.Punch;
import shared.gameObjects.weapons.Weapon;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class Player extends GameObject {

  /**
   * The speed of the player in pixels per update frame
   */
  protected static final float speed = 9;
  /**
   * The jump force of the player in Newtons
   */
  protected static final float jumpForce = -300;
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
  /** True when the gun is aiming LHS */
  protected boolean aimLeft;
  /** True when the mouse pointer is on the LHS */
  protected boolean pointLeft;
  /**
   * The current health of the player; is killed when reaches 0
   */
  protected int health;
  /**
   * Max health
   */
  protected final int maxHealth = 200;
  /**
   * The current weapon the player is using
   */
  protected Weapon holding;
  /**
   * When not holding a weapon, it defaults to the "Punch" weapon
   */
  protected Weapon myPunch;
  /**
   *
   */
  protected boolean damagedThisFrame;
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
  private int animationTimer = 0; //This is used to synchronise the animations for each limb.


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
    this.health = maxHealth;
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
    addPunch();
  }

  private void addLimbs() {
    legLeft = new Leg(true, this, settings.getLevelHandler());
    legRight = new Leg(false, this, settings.getLevelHandler());
    body = new Body(this, settings.getLevelHandler());
    head = new Head(this, settings.getLevelHandler());
    armLeft = new Arm(true, this, settings.getLevelHandler());
    armRight = new Arm(false, this, settings.getLevelHandler());
    handLeft = new Hand(true, armLeft, this, settings.getLevelHandler());
    handRight = new Hand(false, armRight,this, settings.getLevelHandler());
    addChild(legLeft);
    addChild(legRight);
    addChild(body);
    addChild(head);
    addChild(armLeft);
    addChild(armRight);
    armRight.addChild(handRight);
    armLeft.addChild(handLeft);
  }

  private void addPunch() {
    myPunch = new Punch(getX(), getY(), "myPunch@Player", this, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(myPunch);
    this.holding = myPunch;
  }

  @Override
  public void addChild(GameObject child) {
    super.addChild(child);
    settings.getLevelHandler().getLimbs().put(child.getUUID(), (Limb) child);
  }

  private void updateAnimationTimer() {
    if(this.behaviour != Behaviour.IDLE) {
      animationTimer++;
    }
    else{
      animationTimer = 0;
    }

  }

  public int getAnimationTimer() {
    return animationTimer;
  }

  @Override
  public void update() {
    checkGrounded(); // Checks if the player is grounded
    badWeapon();
    pointLeft = mouseX < this.getX();
    updateAnimationTimer();
    if (deattach) {
      for (int i = 0; i < 6; i++) {
        Limb test = (Limb) children.get(i);
        test.detachLimb();
      }
    }
    damagedThisFrame = false;
    super.update();
  }

  @Override
  public String getState() {
    return objectUUID + ";" + id + ";" + getX() + ";" + getY() + ";" + animation.getName() + ";"
        + health + ";"
        + lastInputCount + ";"
        + throwHoldingKey;
  }

  @Override
  public void setState(String data, Boolean snap) {
    super.setState(data, snap);
    String[] unpackedData = data.split(";");
    //this.animation.switchAnimation(unpackedData[4]);
    this.health = Integer.parseInt(unpackedData[5]);
    this.lastInputCount = Integer.parseInt(unpackedData[6]);
    this.throwHoldingKey = Boolean.parseBoolean(unpackedData[7]);
  }

  private void checkGrounded() {
    grounded = rb.isGrounded();
  }

  /**
   * Applies the inputs at the beginning of the frame
   */
  public void applyInput() {
    if (grounded) {
      jumped = false;
    }
    if (rightKey) {
      rb.moveX(speed);
      createWalkParticle();
      behaviour = Behaviour.WALK_RIGHT;
    }
    if (leftKey) {
      rb.moveX(speed * -1);
      createWalkParticle();
      behaviour = Behaviour.WALK_LEFT;
    }

    if (!rightKey && !leftKey) {
      vx = 0;
      behaviour = Behaviour.IDLE;
    }
    if (jumpKey && !jumped && grounded) {
      rb.moveY(jumpForce * (legLeft.limbAttached && legRight.limbAttached ? 1f : 0.7f), 0.33333f);
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

    if (click && holding != null && !(!armLeft.limbAttached || !armRight.limbAttached)) {
      holding.fire(mouseX, mouseY);
    }
    // setX(getX() + (vx * 0.0166));
  }

  private void createWalkParticle() {
    if(!grounded) return;
      settings.getLevelHandler().addGameObject(new Particle(transform.getBotPos().sub(transform.getSize().mult(new Vector2(0.5, 0))), new Vector2(0, -35), new Vector2(0, 100), new Vector2(8,8),
          "images/platforms/stone/elementStone001.png", 0.34f));
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
    try {
      if (!armLeft.limbAttached || !armRight.limbAttached) {
        this.throwHolding();
      }
    } catch (Exception e) {

    }
    return false;
  }

  /**
   * Throws the weapon currently held with a velocity
   */
  public void throwHolding() {
    if (!(this.holding == null || this.holding instanceof Punch)) {
      Weapon w = this.holding;
      w.startThrowing();
      throwHoldingKey = false;
      this.usePunch();
    }
  }

  @Override
  public void interpolatePosition(float alpha) {

  }

  public void deductHp(int damage) {
    if(!damagedThisFrame) {
      damagedThisFrame = true;
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
  }


  /**
   * Resets the player's values, a "respawn"
   */
  public void reset() {
    health = maxHealth;
    if (this.active == false) {
      this.imageView.setRotate(0);
      this.imageView.setTranslateY(getY() - 70);
      this.setActive(true);
      this.bc.setLayer(ColliderLayer.PLAYER);
    }
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
    try {
      if (!armLeft.limbAttached || !armRight.limbAttached) return;
    } catch (Exception e) {

    }
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

  public boolean isAimingLeft() {
    return this.aimLeft;
  }

  public void setAimingLeft(boolean b) {
    this.aimLeft = b;
  }

  public boolean isPointingLeft() {
    return this.pointLeft;
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

  public Limb getHead() {
    return head;
  }

  public Limb getBody() {
    return body;
  }

  public Limb getLegLeft() {
    return legLeft;
  }

  public Limb getLegRight() {
    return legRight;
  }

  public Limb getArmLeft() {
    return armLeft;
  }

  public Limb getArmRight() {
    return armRight;
  }

  public Limb getHandLeft() {
    return handLeft;
  }

  public Limb getHandRight() {
    return handRight;
  }
}
