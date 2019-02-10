package shared.gameObjects.players;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import java.util.UUID;
import javafx.scene.image.Image;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.weapons.Weapon;
import shared.packets.PacketInput;
import shared.physics.Physics;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.Path;
import shared.util.maths.Vector2;

public class Player extends GameObject {

  protected final float speed = 10;
  protected final float jumpForce = 5;
  protected int health;
  protected Weapon holding;
  private Rigidbody rb;
  private double vx;

  public Player(double x, double y, double sizeX, double sizeY, UUID playerUUID) {
    super(x, y, 120, 120, ObjectID.Player, playerUUID);
    addComponent(new BoxCollider(this, false));
    rb = new Rigidbody(RigidbodyType.DYNAMIC, 100, 10, 0.3f, new MaterialProperty(0.1f, 0, 0.2f), null, this);
    addComponent(rb);
    this.health = 100;
    holding = null;
  }

  // Initialise the animation 
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/player_idle.png");
    this.animation.supplyAnimation("moveLeft",
        "images/player/player_left_walk1.png",
        "images/player/player_left_walk2.png");
    this.animation.supplyAnimation("moveRight",
        "images/player/player_right_walk1.png",
        "images/player/player_right_walk2.png");
  }

  @Override
  public void update() {
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
    return null;
  }

  public void applyInput(boolean multiplayer, ConnectionHandler connectionHandler) {
    if (InputHandler.rightKey) {
      rb.moveX(speed);
      animation.switchAnimation("moveRight");
    }
    if (InputHandler.leftKey) {
      rb.moveX(speed*-1);
      animation.switchAnimation("moveLeft");
    }

    if (!InputHandler.rightKey && !InputHandler.leftKey) {
      vx = 0;
      animation.switchDefault();
    }
    if(InputHandler.jumpKey) {
      rb.setVelocity(new Vector2(rb.getVelocity().getX(), jumpForce*-1));
    }
    if (InputHandler.click && holding != null) {
      System.out.println("@Player, input("+InputHandler.x+", "+InputHandler.y+")");
      holding.fire(InputHandler.x, InputHandler.y);
    } // else punch
    //setX(getX() + (vx * 0.0166));

    if (this.getHolding() != null) {
      this.getHolding().setX(this.getX());
      this.getHolding().setY(this.getY());
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
