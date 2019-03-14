package shared.gameObjects.players;

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
import shared.gameObjects.weapons.Sword;
import shared.gameObjects.weapons.Weapon;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class Player extends GameObject implements Destructable {

  protected final float speed = 9;
  protected final float jumpForce = -300;
  protected final float JUMP_LIMIT = 2.0f;
  public boolean leftKey, rightKey, jumpKey, click;
  //Testing
  public boolean deattach;
  public double mouseX, mouseY;
  public int score;
  protected Behaviour behaviour;
  protected float jumpTime;
  protected boolean jumped;
  protected boolean grounded;
  protected boolean facingLeft;
  protected boolean facingRight;
  protected int health;
  protected Weapon holding;
  protected Rigidbody rb;
  protected double vx;
  private BoxCollider bc;
  private ObjectShake shake;

  // Limbs
  private Limb head;
  private Limb body;
  private Limb legLeft;
  private Limb legRight;
  private Limb armLeft;
  private Limb armRight;
  private Limb handLeft;
  private Limb handRight;

  private CircleCollider cc;

  //Networking
  private int lastInputCount;

  public Player(double x, double y, UUID playerUUID) {
    super(x, y, 80, 110, ObjectType.Player, playerUUID);
    this.lastInputCount = 0;
    this.score = 0;
    this.leftKey = false;
    this.rightKey = false;
    this.jumpKey = false;
    this.click = false;
    this.health = 100;
    this.holding = null;
    this.behaviour = Behaviour.IDLE;
    this.shake = new ObjectShake(this);
    this.bc = new BoxCollider(this, ColliderLayer.PLAYER, false);
    //  this.cc = new CircleCollider(this, ColliderLayer.PLAYER, transform.getSize().magnitude()*0.5f, false);
    this.rb = new Rigidbody(RigidbodyType.DYNAMIC, 90, 11.67f, 0.2f,
        new MaterialProperty(0f, 0.1f, 0.05f), null, this);
    //  addComponent(cc);
    addComponent(bc);
    addComponent(rb);
    //addComponent(shake);
  }

  // Initialise the animation
  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/player_idle.png");
  }

  public void addChild(GameObject child) {
    children.add(child);
    levelHandler.addGameObject(child);
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    addLimbs();
  }

  private void addLimbs() {
    legLeft = new Leg(true, this, levelHandler);
    legRight = new Leg(false, this, levelHandler);
    body = new Body(this, levelHandler);
    head = new Head(this, levelHandler);
    armLeft = new Arm(true, this, levelHandler);
    armRight = new Arm(false, this, levelHandler);
    handLeft = new Hand(true, armLeft, levelHandler);
    handRight = new Hand(false, armRight, levelHandler);
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
      for (int i = 0; i < 8; i++) {
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
    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    } // else punch
    // setX(getX() + (vx * 0.0166));

    if (this.getHolding() != null) {
      if (this.getHolding().isMelee()) {
        this.getHolding().setX(((Sword) this.getHolding()).getGripX());
        this.getHolding().setY(((Sword) this.getHolding()).getGripY());
      } else if (this.getHolding().isGun()) {
        this.getHolding().setX(((MachineGun) this.getHolding()).getGripX());
        this.getHolding().setY(((MachineGun) this.getHolding()).getGripY());
      }
    }
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
      this.setHolding(null);
      Weapon sword =
          new Sword(this.getX(), this.getY(), "newSword@Player", this, UUID.randomUUID());
      sword.initialise(root);
      levelHandler.addGameObject(sword);
      this.setHolding(sword);
      return true;
    }
    return false;
  }

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

  public void increaseScore() {
    score++;
  }

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
    return holding;
  }

  public void setHolding(Weapon holding) {
    this.holding = holding;
    if (holding != null) {
      holding.setSettings(settings);
    }
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
    if (facingLeft) {
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
    if (facingLeft) {
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
