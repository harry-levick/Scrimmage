package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;

/**
 * Circle bullet class
 */
public class CircleBullet extends Bullet {

  /** Width of the bullet */
  private static final int width = 15;
  /** Damage of the bullet */
  private static final int damage = 5;
  /** Speed of the bullet */
  private static final int speed = 50;
  /** Path to image */
  private static String imagePath = "images/weapons/circleBullet.png";

  /**
   * Constructor of a circle bullet
   *
   * @param gunX X position of the gun when fired
   * @param gunY Y position of the gun when fired
   * @param mouseX X position of the mouse when fired
   * @param mouseY Y position of the mouse when fired
   * @param holder The player who fired this bullet
   * @param uuid UUID of this bullet
   */
  public CircleBullet(
      double gunX,
      double gunY,
      double mouseX,
      double mouseY,
      Player holder,
      UUID uuid) {

    super(gunX, gunY, mouseX, mouseY, width, speed, damage, holder, uuid);
  }

  @Override
  public void initialiseAnimation() {
    // this.animation.supplyAnimation("default", this.imagePath);
    this.animation.supplyAnimationWithSize(
        "default", this.getWidth(), this.getWidth(), true, Path.convert(this.imagePath));
  }

}