package shared.gameObjects.players;

import client.handlers.inputHandler.KeyboardInput;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Weapon;

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
  public void interpolatePosition(float alpha) {
    if (!isActive()) {
      return;
    }
    imageView.setTranslateX(alpha * getX() + (1 - alpha) * imageView.getTranslateX());
    imageView.setTranslateY(alpha * getY() + (1 - alpha) * imageView.getTranslateY());
  }

  @Override
  public String getState() {
    return null;
  }

  public void applyInput() {
    if (KeyboardInput.rightKey) {
      vx = speed;
    }
    if (KeyboardInput.leftKey) {
      vx = -speed;
    }
    if (!KeyboardInput.rightKey && !KeyboardInput.leftKey) {
      vx = 0;
    }
    setX(getX() + (vx * 0.0166));
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
