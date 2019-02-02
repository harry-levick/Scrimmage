package shared.gameObjects.players;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Weapon;
import shared.packets.PacketInput;

public class Player extends GameObject {

  protected int health;
  protected Weapon holding;
  protected final int speed = 500;
  private double vx;

  public Player(double x, double y, UUID playerUUID) {
    super(x, y, ObjectID.Player, "images/player/player_idle.png", playerUUID);
    this.health = 100;
    holding = null;
  }

  // These are just temporary before physics gets implemented

  @Override
  public void update() {

  }

  @Override
  public void render() {
    if (!isActive()) {
      return;
    }
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
    }
    if (InputHandler.leftKey) {
      vx = -speed;
    }
    if (!InputHandler.rightKey && !InputHandler.leftKey) {
      vx = 0;
    }
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
