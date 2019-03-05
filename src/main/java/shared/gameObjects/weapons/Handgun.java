package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.Path;

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
  public Handgun(
      double x, double y, double sizeX, double sizeY, String name, Player holder, UUID uuid) {
    super(
        x,
        y,
        sizeX,
        sizeY,
        ObjectType.Weapon, // ObjectType
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
          new CircleBullet(
              getX(),
              getY(),
              mouseX,
              mouseY,
              this.holder,
              uuid);
      this.currentCooldown = getDefaultCoolDown();
      deductAmmo();
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert("images/weapons/handgun.jpg"));
  }
}
