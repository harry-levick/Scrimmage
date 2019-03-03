package shared.gameObjects.players;

import client.main.Client;
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
import shared.gameObjects.weapons.Sword;
import shared.gameObjects.weapons.Weapon;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;

public class Player extends GameObject {

  protected final float speed = 9;
  protected final float jumpForce = -200;
  protected final float JUMP_LIMIT = 2.0f;
  public boolean leftKey, rightKey, jumpKey, click;
  //Testing
  public boolean deattach;
  public double mouseX, mouseY;
  public int score;
  protected LevelHandler levelHandler;
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

  //Networking
  private int lastInputCount;

  public Player(double x, double y, UUID playerUUID, LevelHandler levelHandler) {
    super(x, y, 80, 110, ObjectType.Player, playerUUID);
    this.lastInputCount = 0;
    this.score = 0;
    this.leftKey = false;
    this.rightKey = false;
    this.jumpKey = false;
    this.click = false;
    this.health = 100;
    this.holding = null;
    this.levelHandler = levelHandler;
    this.behaviour = Behaviour.IDLE;
    this.bc = new BoxCollider(this, ColliderLayer.PLAYER, false);
    this.rb = new Rigidbody(RigidbodyType.DYNAMIC, 80, 8, 0.2f,
        new MaterialProperty(0.005f, 0.1f, 0.05f), null, this);
    addComponent(bc);
    addComponent(rb);
  }

  // Initialise the animation
  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/player_idle.png");
  }

  @Override
  public void addChild(GameObject child) {
    children.add(child);
    levelHandler.addGameObject(child);
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    addChild(new Leg(true, this, levelHandler));
    addChild(new Leg(false, this, levelHandler));
    addChild(new Body(this, levelHandler));
    addChild(new Head(this, levelHandler));
    Arm rightArm = new Arm(false, this, levelHandler);
    Arm leftArm = (new Arm(true, this, levelHandler));
    addChild(rightArm);
    addChild(leftArm);
    rightArm.addChild(new Hand(false, rightArm, levelHandler));
    leftArm.addChild(new Hand(true, leftArm, levelHandler));
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

  public void checkGrounded() {
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
    if (jumpKey && !jumped) {
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
      this.getHolding().setX(this.getX() + 60);
      this.getHolding().setY(this.getY() + 70);
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
      Client.levelHandler.addGameObject(sword);
      this.setHolding(sword);
      return true;
    }
    return false;
  }

  public void deductHp(int damage) {
    this.health -= damage;
    if (this.health <= 0) {
      // For testing
      this.imageView.setTranslateY(getY() + 70);
      this.setActive(false);
      this.removeComponent(bc);
      this.imageView.setRotate(90);
    }
  }

  public void reset() {
    health = 100;
    if (this.active == false) {
      this.imageView.setRotate(0);
      this.imageView.setTranslateY(getY() - 70);
      this.setActive(true);
      this.addComponent(bc);
    }
    children.forEach(child -> {
      Limb limb = (Limb) child;
      limb.reset();
    });
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

  public double[] getHandPos() {
    if (jumped && facingLeft) {
      return new double[]{this.getHandLeftJumpX(), this.getHandLeftJumpY()};
    } else if (jumped && facingRight) {
      return new double[]{this.getHandRightJumpX(), this.getHandRightJumpY()};
    } else if (facingLeft) {
      return new double[]{this.getHandLeftX(), this.getHandLeftY()};
    } else if (facingRight) {
      return new double[]{this.getHandRightX(), this.getHandRightY()};
    }
    return new double[]{this.getHandRightX(), this.getHandRightY()};
  }

  /**
   * Hand position x of the player when facing left
   *
   * @return x position of the hand
   */
  public double getHandLeftX() {
    return this.getX() + 13;
  }

  /**
   * Hand position y of the player when facing left
   *
   * @return y position of the hand
   */
  public double getHandLeftY() {
    return this.getY() + 90;
  }

  /**
   * Hand position x of the player when facing right
   *
   * @return x position of the hand
   */
  public double getHandRightX() {
    return this.getX() + 60;
  }

  /**
   * Hand position y of the player when facing right
   *
   * @return y position of the hand
   */
  public double getHandRightY() {
    return this.getY() + 90;
  }

  /**
   * Hand position x of the player when jumping and facing left
   *
   * @return x position of the hand
   */
  public double getHandLeftJumpX() {
    return this.getX() + 7;
  }

  /**
   * Hand position y of the player when jumping and facing left
   *
   * @return y position of the hand
   */
  public double getHandLeftJumpY() {
    return this.getY() + 30;
  }

  /**
   * Hand position x of the player when jumping and facing right
   *
   * @return x position of the hand
   */
  public double getHandRightJumpX() {
    return this.getX() + 67;
  }

  /**
   * Hand position y of the player when jumping and facing right
   *
   * @return y position of the hand
   */
  public double getHandRightJumpY() {
    return this.getY() + 30;
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

  public Behaviour getBehaviour() {
    return behaviour;
  }

  public void setBehaviour(Behaviour behaviour) {
    this.behaviour = behaviour;
  }

  @Override
  public void OnCollisionEnter(Collision col) {
    //  System.out.println("Entered Collision!");
  }

  @Override
  public void OnCollisionExit(Collision col) {

  }

  @Override
  public void OnCollisionStay(Collision col) {
    //  System.out.println("Stayed in Collision!");
  }

  public int getLastInputCount() {
    return lastInputCount;
  }

  public void setLastInputCount(int lastInputCount) {
    this.lastInputCount = lastInputCount;
  }
}
