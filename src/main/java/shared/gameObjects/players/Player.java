package shared.gameObjects.players;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;
import javafx.scene.image.Image;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Weapon;
import shared.packets.PacketInput;

public class Player extends GameObject {

  protected int health;
  protected Weapon holding;
  protected final int speed = 500;
  private double vx;

  public Player(double x, double y, double sizeX, double sizeY, UUID playerUUID) {
    super(x, y, 100, 100, ObjectID.Player, playerUUID);
    this.health = 100;
    holding = null;
  }
  
  // Initialise the animation 
  public void initialiseAnimation() {
    try {
      Image[] insertImageList = new Image[0];
      insertImageList = new Image[]{
          new Image(new FileInputStream("resources/images/player/player_idle.png"))
      };
      this.animation.supplyAnimation("default", insertImageList);

      //Running left animation
      insertImageList = new Image[]{
          new Image(new FileInputStream("resources/images/player/player_left_walk1.png")),
          new Image(new FileInputStream("resources/images/player/player_left_walk2.png")),
      };
      this.animation.supplyAnimation("moveLeft", insertImageList);

      //Running right animation
      insertImageList = new Image[]{
          new Image(new FileInputStream("resources/images/player/player_right_walk1.png")),
          new Image(new FileInputStream("resources/images/player/player_right_walk2.png")),
      };
      this.animation.supplyAnimation("moveRight", insertImageList);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
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
      vx = speed;
      animation.switchAnimation("moveRight");
    }
    if (InputHandler.leftKey) {
      vx = -speed;
      animation.switchAnimation("moveLeft");
    }
  
    if (!InputHandler.rightKey && !InputHandler.leftKey) {
      vx = 0;
      animation.switchDefault();
    }
    if (InputHandler.click && holding != null) {
      holding.fire(InputHandler.x, InputHandler.y);
    } //else punch
    setX(getX() + (vx * 0.0166));

    /** If multiplayer then send input to server */
    if (multiplayer) {
      PacketInput input = new PacketInput(InputHandler.x, InputHandler.y, InputHandler.leftKey,
          InputHandler.rightKey, InputHandler.jumpKey, InputHandler.click);
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
