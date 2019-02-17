package shared.gameObjects.weapons;

import java.util.UUID;

public class MachineGunBullet extends Bullet {

  private static String imagePath = "images/weapons/bullet.png";

  public MachineGunBullet(
      double gunX,
      double gunY,
      double sizeX,
      double sizeY,
      double mouseX,
      double mouseY,
      double width,
      double speed,
      int damage,
      UUID uuid) {

    super(gunX, gunY, sizeX, sizeY, mouseX, mouseY, width, speed, damage, uuid);
  }

  @Override
  public void initialiseAnimation() {
    // this.animation.supplyAnimation("default", this.imagePath);
    this.animation.supplyAnimationWithSize(
        "default", this.getWidth(), this.getWidth(), true, this.imagePath);
  }
}
