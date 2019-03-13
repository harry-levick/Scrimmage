package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.Path;

/**
 * @author hlf764 The Handgun class.
 */
public class Handgun extends Gun {

  private double[] holderHandPos;
  private double angleGun;

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
        10, // weight
        name,
        30, // ammo
        50, // fireRate
        holder,
        false, // fullAutoFire
        true, // singleHanded
        uuid);
  }

  public Handgun(Handgun that) {
    this(that.getX(), that.getY(), that.getTransform().getSize().getX(),
        that.getTransform().getSize().getY(), that.name, that.holder, UUID.randomUUID());
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

  // TODO: *****All these getters copied from MachineGun, not calibrated with
  //       handgun yet *****
  @Override
  public double getForeGripX() {
    if (holder.isAimingLeft()) {
      return getForeGripFlipX();
    }
    return getGripX() + 50 * Math.cos(-angleGun);
  }

  @Override
  public double getForeGripY() {
    if (holder.isAimingLeft()) {
      return getForeGripFlipY();
    }
    return getGripY() + 50 * Math.sin(angleGun);
  }

  @Override
  public double getForeGripFlipX() {
    return getGripX() + 50 - 30 * Math.cos(angleGun);
  }

  @Override
  public double getForeGripFlipY() {
    return getGripY() - 50 * Math.sin(angleGun);
  }

  @Override
  public double getGripX() {
    if (holder.isAimingLeft()) {
      return getGripFlipX();
    } else {
      return holderHandPos == null ? 0 : holderHandPos[0] - 20;
    }
  }

  @Override
  public double getGripY() {
    if (holder.isAimingLeft()) {
      return getGripFlipY();
    } else {
      return holderHandPos == null ? 0 : holderHandPos[1] - 20;
    }
  }

  @Override
  public double getGripFlipX() {
    return holderHandPos[0] - 55;
  }

  @Override
  public double getGripFlipY() {
    return holderHandPos[1] - 10;
  }
}
