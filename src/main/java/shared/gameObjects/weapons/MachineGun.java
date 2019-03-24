package shared.gameObjects.weapons;

import client.handlers.audioHandler.AudioHandler;
import client.main.Client;
import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;
import shared.util.maths.Vector2;

/**
 * Class of Machine Gun
 */
public class MachineGun extends Gun {

  private static String imagePath = "images/weapons/machinegun.png"; // path to Machine Gun image
  private static String audioPath = "audio/sound-effects/laser_gun.wav"; // path to Machine Gun sfx
  private static double sizeX = 84, sizeY = 35;

  /**
   * Default Constructor of a machine gun
   *
   * @param x X position of the gun
   * @param y Y position of the gun
   * @param name Name of the gun
   * @param holder The player holding this gun
   * @param uuid UUID of this gun
   */
  public MachineGun(double x, double y, String name, Player holder, UUID uuid) {

    super(
        x,
        y,
        sizeX, // sizeX
        sizeY, // sizeY
        10, // weight
        name,
        50, // ammo
        70, // fireRate
        20, // pivotX
        10, // pivotY
        holder,
        true, // fullAutoFire
        false, // singleHanded
        uuid);
    this.weaponRank = 3;
  }

  /**
   * Constructor for AI
   *
   * @param that A copy of this gun with different UUID
   */
  public MachineGun(MachineGun that) {
    this(that.getX(), that.getY(), that.name, that.holder, UUID.randomUUID());
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      Vector2 playerCentre = new Vector2(holderHandPos[0], holderHandPos[1]); // centre = main hand
      double playerRadius = 55 + 65; // Player.sizeY / 2 + bias

      double bulletX = playerCentre.getX() + playerRadius * Math.cos(-angleRadian);
      double bulletY = playerCentre.getY() - playerRadius * Math.sin(-angleRadian);
      double bulletFlipX = playerCentre.getX() - playerRadius * Math.cos(angleRadian);
      double bulletFlipY = playerCentre.getY() - playerRadius * Math.sin(angleRadian);
      Bullet bullet =
          new FireBullet(
              (holder.isAimingLeft() ? bulletFlipX : bulletX),
              (holder.isAimingLeft() ? bulletFlipY : bulletY),
              mouseX,
              mouseY,
              this.holder,
              uuid);
      settings.getLevelHandler().addGameObject(bullet);
      this.currentCooldown = getDefaultCoolDown();
      new AudioHandler(settings, Client.musicActive).playSFX("MACHINEGUN");
      deductAmmo();
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert(this.imagePath));
  }

  @Override
  public boolean firesExplosive() {
    return false;
  }

  // =============================
  // Get Grip and Muzzle positions
  // =============================
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

  @Override
  public double getForeGripX() {
    if (holder.isAimingLeft()) {
      return getForeGripFlipX();
    }
    return getGripX() + 50 * Math.cos(-angleRadian);
  }

  @Override
  public double getForeGripY() {
    if (holder.isAimingLeft()) {
      return getForeGripFlipY();
    }
    return getGripY() + 50 * Math.sin(angleRadian);
  }

  @Override
  public double getForeGripFlipX() {
    return getGripX() + 50 - 30 * Math.cos(angleRadian);
  }

  @Override
  public double getForeGripFlipY() {
    return getGripY() - 50 * Math.sin(angleRadian);
  }
}
