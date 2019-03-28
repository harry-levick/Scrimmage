package shared.gameObjects.weapons;

import client.handlers.audioHandler.AudioHandler;
import client.main.Client;
import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;
import shared.util.maths.Vector2;

/**
 * Explosive launcher class
 */
public class ExplosiveLauncher extends Gun {

  /**
   * Path to image
   */
  private static String imagePath = "images/weapons/explosiveLauncher.png";
  /**
   * Size of image
   */
  private static double sizeX = 117, sizeY = 29;
  /** Ammo of gun */
  private static int ammo = 10;
  /** Fire rate of gun */
  private static int fireRate = 20;

  /**
   * Constructor
   *
   * @param x X position of the explosive launcher
   * @param y Y position of the explosive launcher
   * @param name Name of the explosive launcher
   * @param holder Player who holds this explosive launcher
   * @param uuid UUID of this explosive launcher
   */
  public ExplosiveLauncher(double x, double y, String name, Player holder, UUID uuid) {
    super(
        x,
        y,
        sizeX, // sizeX
        sizeY, // sizeY
        10, // weight
        name,
        ammo,
        fireRate,
        25, // pivotX
        10, // pivotY
        holder,
        false, // fullAutoFire
        false, // singleHanded
        uuid);

    this.weaponRank = 4;
  }

  /**
   * Constructor for AI
   *
   * @param that A copy of this explosive launcher with different UUID
   */
  public ExplosiveLauncher(ExplosiveLauncher that) {
    this(that.getX(), that.getY(), that.name, that.holder, UUID.randomUUID());
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      Vector2 playerCentre = new Vector2(holderHandPos[0], holderHandPos[1]); // centre = main hand

      double bulletX;
      double bulletY;

      try {
        if (holder.isAimingLeft()) {
          bulletX = playerCentre.getX() - playerRadius * Math.cos(angleRadian);
          bulletY = playerCentre.getY() - playerRadius * Math.sin(angleRadian);
        } else {
          bulletX = playerCentre.getX() + playerRadius * Math.cos(-angleRadian);
          bulletY = playerCentre.getY() - playerRadius * Math.sin(-angleRadian);
        }

        // Ray cast check if shooting floor
        double[] bulletStartPos =
            isShootingFloor(bulletX, bulletY, mouseX, mouseY, playerCentre);

        Bullet bullet =
            new ExplosiveBullet(
                bulletStartPos[0],
                bulletStartPos[1],
                mouseX,
                mouseY,
                this.holder,
                uuid);

        settings.getLevelHandler().addGameObject(bullet);
      } catch (NullPointerException e) {
        System.out.println("NullPointerException in creating bullet in ExplosiveLauncher");
      }
      this.currentCooldown = getDefaultCoolDown();
      new AudioHandler(settings, Client.musicActive).playSFX("LASER");
      deductAmmo();
    }
  }

  @Override
  public boolean firesExplosive() {
    return true;
  }

  @Override
  public double getGripX() {
    if (holder.isAimingLeft()) {
      return getGripFlipX();
    } else {
      return holderHandPos == null ? 0 : holderHandPos[0] - 15;
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
    return holderHandPos[0] - 85;
  }

  @Override
  public double getGripFlipY() {
    return holderHandPos[1] - 20;
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

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert(imagePath));
  }
}
