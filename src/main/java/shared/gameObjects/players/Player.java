package shared.gameObjects.players;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import client.main.Client;
import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.weapons.Sword;
import shared.gameObjects.weapons.Weapon;
import shared.packets.PacketInput;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class Player extends GameObject {

  protected final float speed = 255;
  protected final float jumpForce = -200;
  protected final float JUMP_LIMIT = 2.0f;
  protected float jumpTime;
  protected boolean jumped;
  protected boolean grounded;
  protected int health;
  protected Weapon holding;
  protected Rigidbody rb;
  protected double vx;

  public Player(double x, double y, double sizeX, double sizeY, UUID playerUUID) {
    super(x, y, sizeX, sizeY, ObjectID.Player, playerUUID);
    addComponent(new BoxCollider(this, false));
    rb = new Rigidbody(RigidbodyType.DYNAMIC, 100, 10, 0.2f, new MaterialProperty(0.005f, 0, 0),
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
    return objectUUID + ";" + getX() + ";" + getY() + ";" + animation.getName() + ";" + health + ";"
        + holding.getUUID();
  }

  @Override
  public void setState(String data) {
    String[] unpackedData = data.split(";");
    setX(Double.parseDouble(unpackedData[1]));
    setY(Double.parseDouble(unpackedData[2]));
    this.animation.switchAnimation(unpackedData[3]);
    this.health = Integer.parseInt(unpackedData[4]);
  }

  public void checkGrounded() {
    ArrayList<Collision> cols = Physics.boxcastAll(
        getTransform().getPos().add(Vector2.Down().mult(getTransform().getSize().getY())),
        getTransform().getSize().mult(new Vector2(1, 0.05f)));
    if (cols.isEmpty()) {
      grounded = false;
    } else {
      for (Collision c : cols) {
        if (c.getCollidedObject().getBodyType() == RigidbodyType.STATIC) {
          grounded = true;
          return;
        }
      }
    }
  }
  public void applyInput(boolean multiplayer, ConnectionHandler connectionHandler) {
    if (InputHandler.rightKey) {
        rb.setVelocity(new Vector2(speed, rb.getVelocity().getY()));
      animation.switchAnimation("walk");
      imageView.setScaleX(1);
    }
    if (InputHandler.leftKey) {
      rb.setVelocity(new Vector2(speed*-1, rb.getVelocity().getY()));
      animation.switchAnimation("walk");
      imageView.setScaleX(-1);
    }

    if (!InputHandler.rightKey && !InputHandler.leftKey) {
      vx = 0;
      animation.switchDefault();

    }
    if (InputHandler.jumpKey && !jumped) {
      rb.moveY(jumpForce, 0.33333f);
      jumped = true;
    }
    if (jumped) {
      animation.switchAnimation("jump");
    }
    if (grounded) {
      jumped = false;
    }
    if (InputHandler.click && holding != null) {
      holding.fire(InputHandler.x, InputHandler.y);
    } //else punch
    //setX(getX() + (vx * 0.0166));

    if (this.getHolding() != null) {
      this.getHolding().setX(this.getX() + 60);
      this.getHolding().setY(this.getY() + 70);
    }

    /** If multiplayer then send input to server */
    if (multiplayer) {
      PacketInput input =
          new PacketInput(
              InputHandler.x,
              InputHandler.y,
              InputHandler.leftKey,
              InputHandler.rightKey,
              InputHandler.jumpKey,
              InputHandler.click);
      connectionHandler.send(input.getData());
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

      Weapon sword = new Sword(this.getX(), this.getY(), 50, 50, "newSword@Player", 10, 50, 20,
          UUID.randomUUID());
      sword.initialise(root);
      Client.levelHandler.addGameObject(sword);
      this.setHolding(sword);
      return true;
    }
    return false;
  }
  
  public void deductHp(Weapon w) {
    this.health -= w.getDamage();
    if (this.health <= 0) {
      // die
    }
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
  }
}
