package shared.gameObjects.players;

import client.handlers.inputHandler.KeyboardInput;
import javafx.scene.image.Image;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Version;
import shared.gameObjects.weapons.Weapon;

public class Player extends GameObject {

  protected int health;
  protected Weapon holding;

  public Player(double x, double y, ObjectID id) {
    super(x, y, id, "images/player/player_idle.png");
    this.health = 100;
    holding = null;
    if (version == Version.CLIENT) {
      createSprites();
    }
  }

  // These are just temporary before physics gets implemented

  @Override
  public void update() {
    if (KeyboardInput.rightKey) {
      setX(getX() + 10);
    }
    if (KeyboardInput.leftKey) {
      setX(getX() - 10);
    }
  }

  @Override
  public void render() {
    imageView.relocate(getX(), getY());
    if (animate) {
      imageView.setImage(animator());
    }
  }

  public void createSprites() {
    spriteLibaryURL.put("player_right_walk1", "images/player/player_right_walk1.png");
    spriteLibaryURL.put("player_right_walk2", "images/player/player_right_walk2.png");
    spriteLibaryURL.put("player_left_walk1", "images/player/player_left_walk1.png");
    spriteLibaryURL.put("player_left_walk2", "images/player/player_left_walk2.png");
  }

  public Image animator() {
    if (KeyboardInput.rightKey) {
      if (imageView.getImage() == spriteLibary.get("player_right_walk1")) {
        return spriteLibary.get("player_right_walk2");
      }
      return spriteLibary.get("player_right_walk1");
    } else if (KeyboardInput.leftKey) {
      if (imageView.getImage() == spriteLibary.get("player_left_walk1")) {
        return spriteLibary.get("player_left_walk2");
      }
      return spriteLibary.get("player_left_walk1");
    }
    return spriteLibary.get("baseImage");
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
