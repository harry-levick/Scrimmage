package shared.gameObjects.weapons;

import java.util.UUID;

/**
 * @author Henry Fung (hlf764)
 */
public class HandgunBullet extends Bullet {

  private static String imagePath = "images/weapons/fireBullet.png";

  public HandgunBullet(
      double gunX,
      double gunY,
      double sizeX,
      double sizeY,
      double mouseX,
      double mouseY,
      double width,
      double speed,
      UUID uuid) {

    super(gunX, gunY, sizeX, sizeY, mouseX, mouseY, width, speed, uuid);
  }

  @Override
  public void initialiseAnimation() {
    //this.animation.supplyAnimation("default", this.imagePath);
    this.animation
        .supplyAnimationWithSize("default", this.getWidth(), this.getWidth(), true, this.imagePath);
  }

}