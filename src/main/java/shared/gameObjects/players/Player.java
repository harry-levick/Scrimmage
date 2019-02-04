package shared.gameObjects.players;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.weapons.Weapon;
import shared.packets.PacketInput;
import javafx.scene.image.Image;

public class Player extends GameObject {

  protected int health;
  protected Weapon holding;
  protected final int speed = 500;
  //private transient Animator animation = new Animator();
  private double vx;

  public Player(double x, double y, UUID playerUUID) {
    super(x, y, ObjectID.Player, "images/player/player_idle.png", playerUUID);
    this.health = 100;
    holding = null;
    System.out.println(animation);
    initializeAnimation();
  }
  
  // Initialise the animation 
  private void initializeAnimation() {
    Image[] insertImageList = {
        new Image("images/player/player_idle.png")  
    };
    this.animation.supplyAnimation("default", insertImageList); 
    
    //Running left animation 
    insertImageList = new Image[]{
        new Image("images/player/player_left_walk1.png"),  
        new Image("images/player/player_left_walk2.png"),  
    };
    this.animation.supplyAnimation("moveLeft", insertImageList);
    
    //Running right animation 
    insertImageList = new Image[]{
        new Image("images/player/player_right_walk1.png"),  
        new Image("images/player/player_right_walk2.png"),  
    };
    this.animation.supplyAnimation("moveRight", insertImageList);
  }


  @Override
  public void update() {
    this.animation.update();
  }

  @Override
  public void render() {
    if (!isActive()) {
      return;
    }
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
    imageView.setImage(animation.getImage());
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
