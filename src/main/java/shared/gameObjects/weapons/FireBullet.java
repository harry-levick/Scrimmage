package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;

/**
 * A bullet that is on fire
 */
public class FireBullet extends Bullet {

  /**
   * Width of the bullet
   */
  private static final int width = 15;
  /**
   * Damage of the bullet
   */
  private static final int damage = 5;
  /** Speed of travel of the bullet */
  private static final int speed = 50;
  /**
   * Path to the image
   */
  private static String imagePath = "images/weapons/fireBullet.png";

  /**
   * Constructor of Fire Bullet
   *
   * @param gunX X position of the gun when fired
   * @param gunY Y position of the gun when fired
   * @param mouseX X position of the mouse when fired
   * @param mouseY Y poistion of the mouse when fired
   * @param holder The player who fired this bullet
   * @param uuid UUID of this bullet
   */
  public FireBullet(
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
