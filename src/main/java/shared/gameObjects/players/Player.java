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

  protected final int speed = 10;
  protected int health;
  protected Weapon holding;
  private Rigidbody rb;
  private double vx;

  public Player(double x, double y, double sizeX, double sizeY, UUID playerUUID) {
    super(x, y, 120, 120, ObjectID.Player, playerUUID);
    addComponent(new BoxCollider(this, false));
    rb = new Rigidbody(RigidbodyType.DYNAMIC, 10, 10, 0, new MaterialProperty(0.1f, 0, 0f), null, this);
    addComponent(rb);
    this.health = 100;
    holding = null;
  }

  // Initialise the animation
  public void initialiseAnimation() {
    Image[] insertImageList = {new Image(Path.convert("images/player/player_idle.png"))};
    this.animation.supplyAnimation("default", insertImageList);

    // Running left animation
    insertImageList =
        new Image[] {
          new Image(Path.convert("images/player/player_left_walk1.png")),
          new Image(Path.convert("images/player/player_left_walk2.png")),
        };
    this.animation.supplyAnimation("moveLeft", insertImageList);

    // Running right animation
    insertImageList =
        new Image[] {
          new Image(Path.convert("images/player/player_right_walk1.png")),
          new Image(Path.convert("images/player/player_right_walk2.png")),
        };
    this.animation.supplyAnimation("moveRight", insertImageList);
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
    if(InputHandler.click) {
      rb.moveY(speed*-1);
    }
    if (InputHandler.click && holding != null) {
      holding.fire(InputHandler.x, InputHandler.y);
    } // else punch
    //setX(getX() + (vx * 0.0166));

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
