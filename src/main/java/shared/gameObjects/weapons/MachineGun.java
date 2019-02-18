package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

public class MachineGun extends Gun {

  private static String imagePath = "images/weapons/machinegun.png"; // path to Machine Gun image

  public MachineGun(double x, double y, double sizeX, double sizeY, String name, Player holder, UUID uuid) {

    super(
        x,
        y,
        sizeX,
        sizeY,
        ObjectID.Weapon, // ObjectID
        5, // damage
        10, // weight
        name,
        50, // ammo
        50, // bulletSpeed
        70, // fireRate
        50, // bulletWidth
        holder,
        true, // fullAutoFire
        false, // singleHanded
        uuid);
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      Bullet bullet =
          new MachineGunBullet(
              getX()+106, getY(), 20, 20, mouseX, mouseY, this.bulletWidth, this.bulletSpeed, this.damage, uuid);
      this.currentCooldown = getDefaultCoolDown();
      deductAmmo();
    }
  }

  @Override
  public void update() {
    super.update();
  }

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());
  }

  @Override
  public void interpolatePosition(float alpha) {
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    // this.animation.supplyAnimation("default", imagePath);
    this.animation.supplyAnimationWithSize("default", 40, 40, true, imagePath);
  }
}
