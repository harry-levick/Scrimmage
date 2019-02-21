package shared.gameObjects.players;

import client.main.Client;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.weapons.Sword;
import shared.gameObjects.weapons.Weapon;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

public class Player extends GameObject {

  protected final float speed = 10;
  protected final float jumpForce = -200;
  protected final float JUMP_LIMIT = 2.0f;
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

  public boolean leftKey, rightKey, jumpKey, click;
  public double mouseX, mouseY;
  public int score;

  public Player(double x, double y, UUID playerUUID) {
    super(x, y, 80, 110, ObjectID.Player, playerUUID);
    score = 0;
    bc = new BoxCollider(this, false);
    addComponent(bc);
    rb = new Rigidbody(RigidbodyType.DYNAMIC, 80, 8, 0.2f, new MaterialProperty(0.005f, 0, 0),
        null, this);
    addComponent(rb);
    this.health = 100;
    holding = null;
  }

  // Initialise the animation
  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/player_idle.png");
    this.animation.supplyAnimation("walk",
        "images/player/player_walk1.png",
        "images/player/player_walk2.png");
    this.animation.supplyAnimation("jump", "images/player/player_jump.png");
  }

  public void initialise(Group root) {
    super.initialise(root);
    this.leftKey = false;
    this.rightKey = false;
    this.jumpKey = false;
    this.click = false;
  }


  @Override
  public void update() {
    checkGrounded(); //Checks if the player is grounded
    // Check if the current holding is valid
    // Change the weapon to Punch if it is not
    badWeapon();
    super.update();
  }


  @Override
  public void render() {
    if (!isActive()) {
      return;
    }
    super.render();
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
  }

  @Override
  public String getState() {
    return objectUUID + ";" + "player" + ";" + getX() + ";" + getY() + ";" + animation.getName()
        + ";" + health;
    //add holding
  }

  @Override
  public void setState(String data) {
    String[] unpackedData = data.split(";");
    setX(Double.parseDouble(unpackedData[2]));
    setY(Double.parseDouble(unpackedData[3]));
    this.animation.switchAnimation(unpackedData[4]);
    this.health = Integer.parseInt(unpackedData[5]);
  }

  public void checkGrounded() {
    grounded = rb.isGrounded();
  }

  public void applyInput() {
    if (rightKey) {
      rb.moveX(speed);
      animation.switchAnimation("walk");
      imageView.setScaleX(1);
      this.facingLeft = false;
      this.facingRight = true;
    }
    if (leftKey) {
      System.out.println("moved from " + getX());
      rb.moveX(speed * -1);
      animation.switchAnimation("walk");
      imageView.setScaleX(-1);
      this.facingRight = false;
      this.facingLeft = true;
      System.out.println("to " + getX());
    }

    if (!rightKey && !leftKey) {
      vx = 0;
      animation.switchDefault();

    }
    if (jumpKey && !jumped) {
      rb.moveY(jumpForce, 0.33333f);
      jumped = true;
    }
    if (jumped) {
      animation.switchAnimation("jump");
    }
    if (grounded) {
      jumped = false;
    }
    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    } //else punch
    //setX(getX() + (vx * 0.0166));

    if (this.getHolding() != null) {
      this.getHolding().setX(this.getX() + 60);
      this.getHolding().setY(this.getY() + 70);
    }
  }

  /**
   * Check if the current holding weapon is valid or not
   *
   * @return True if the weapon is a bad weapon (out of ammo)
   * @return False if the weapon is a good weapon, or there is no weapon
   */
  public boolean badWeapon() {
    if (this.holding == null) {
      return false;
    }
    if (this.holding.getAmmo() == 0) {
      this.holding.destroyWeapon();
      this.setHolding(null);

      Weapon sword = new Sword(this.getX(), this.getY(), "newSword@Player", this,
          UUID.randomUUID());
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
      //For testing
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


  }

  public void increaseScore() {
    score++;
  }

  public void increaseScore(int amount) {
    score += amount;
  }

  public void setHealth(int hp) {
    this.health = hp;
  }

  public int getHealth() {
    return health;
  }

  public Weapon getHolding() {
    return holding;
  }

  public void setHolding(Weapon holding) {
    this.holding = holding;
    holding.setSettings(settings);
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
  
  public boolean getFacingRight() {
    return this.facingRight;
  }
}
