package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;

/**
 * @author Henry Fung (hlf764)
 */
public class HandgunBullet extends Bullet {

  private static String imagePath = "images/weapons/fireBullet.png";

  public HandgunBullet(
      double gunX,
      double gunY,
      double mouseX,
      double mouseY,
      double width,
      double speed,
      int damage,
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
