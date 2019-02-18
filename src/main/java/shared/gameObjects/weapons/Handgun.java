package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

/**
 * @author hlf764 The Handgun class.
 */
public class Handgun extends Gun {

  /**
   * Constructor of the Handgun class
   *
   * @param x The x position of the gun
   * @param y The y position of the gun
   * @param name Name of the gun
   * @param uuid UUID of the gun
   */
  public Handgun(double x, double y, double sizeX, double sizeY, String name, Player holder, UUID uuid) {
    super(
        x,
        y,
        sizeX,
        sizeY,
        ObjectID.Weapon, // ObjectID
        10, // damage
        10, // weight
        name,
        30, // ammo
        1, // bulletSpeed
        50, // fireRate
        50, // bulletWidth
        holder,
        false, // fullAutoFire
        true, // singleHanded
        uuid);
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      Bullet bullet =
          new HandgunBullet(
              getX(), getY(), 10, 10, mouseX, mouseY, this.bulletWidth, this.bulletSpeed, this.damage, this.holder, uuid);
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
    this.animation.supplyAnimation("default", "images/weapons/handgun.jpg");
  }
}
