package shared.gameObjects.weapons;

import client.handlers.audioHandler.AudioHandler;
import client.main.Client;
import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;
import shared.util.maths.Vector2;

public class ExplosiveLauncher extends Gun {

  private static String imagePath = "images/weapons/explosiveLauncher.png";
  private static String audioPath = "audio/sound-effects/laser_gun.wav";
  private static double sizeX = 50, sizeY = 50;

  public ExplosiveLauncher(double x, double y, String name, Player holder, UUID uuid) {
    super (
        x,
        y,
        sizeX, // sizeX
        sizeY, // sizeY
        10, // weight
        name,
        10, // ammo
        40, // fireRate
        20, // pivotX
        10, // pivotY
        holder,
        false, // fullAutoFire
        false, // singleHanded
        uuid);

    this.weaponRank = 10;
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
          new ExplosiveBullet(
              (holder.isAimingLeft() ? bulletFlipX : bulletX),
              (holder.isAimingLeft() ? bulletFlipY : bulletY),
              mouseX,
              mouseY,
              this.holder,
              uuid);
      settings.getLevelHandler().addGameObject(bullet);
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
    return 0;
  }

  @Override
  public double getGripY() {
    return 0;
  }

  @Override
  public double getGripFlipX() {
    return 0;
  }

  @Override
  public double getGripFlipY() {
    return 0;
  }

  @Override
  public double getForeGripX() {
    return 0;
  }

  @Override
  public double getForeGripY() {
    return 0;
  }

  @Override
  public double getForeGripFlipX() {
    return 0;
  }

  @Override
  public double getForeGripFlipY() {
    return 0;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert(imagePath));
  }
}
