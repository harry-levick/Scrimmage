package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.maths.Vector2;

/**
 * @author hlf764 The abstract class for all guns type weapon.
 */
public abstract class Gun extends Weapon {

  protected float aimAngleMax = 110f; // Maximum angle of aiming before switching holding hand

  protected double bulletSpeed; // pixel per second
  protected int fireRate; // bullets per minute
  protected double bulletWidth; // width of bullet ==(pixel?)==
  protected boolean fullAutoFire; // able to shoot with full-auto or single-shot
  protected boolean singleHanded; // holding the weapon with one hand or two
  protected int ammo; // The amount of ammo left

  /**
   * Constructor of the Gun class
   *
   * @param x The x position of the gun
   * @param y The y position of the gun
   * @param weight Weight of the gun
   * @param name Name of the gun
   * @param ammo Total amount of ammo
   * @param fireRate Fire rate of the gun (bullets per minute)
   * @param fullAutoFire Is it full-automatic fire or single-shot
   * @param singleHanded Is it be hold with one hand or two hands
   */
  public Gun(
      double x,
      double y,
      double sizeX,
      double sizeY,
      double weight,
      String name,
      int ammo,
      int fireRate,
      Player holder,
      boolean fullAutoFire,
      boolean singleHanded,
      UUID uuid) {

    super(x, y, sizeX, sizeY, ObjectType.Weapon, weight, name, true, false, ammo, fireRate, holder,
        uuid);

    this.fullAutoFire = fullAutoFire;
    this.singleHanded = singleHanded;
  }

  public abstract double getForeGripX();

  public abstract double getForeGripY();

  public abstract double getForeGripFlipX();

  public abstract double getForeGripFlipY();

  @Override
  public void update() {
    deductCooldown();
    super.update();
  }

  @Override
  public void render() {
    super.render();

    if (holder == null) {
      return;
    }

    imageView.getTransforms().clear();

    double mouseX = holder.mouseX;
    double mouseY = holder.mouseY;
    Vector2 mouseV = new Vector2((float) mouseX, (float) mouseY);
    Vector2 gripV = new Vector2((float) this.getGripX(), (float) this.getGripY());
    Vector2 mouseSubGrip = mouseV.sub(gripV);
    angleRadian = mouseSubGrip.normalize().angleBetween(Vector2.Zero());  // radian
    double angle = angleRadian * 180 / PI;  // degree

    // Change the facing of the player when aiming the other way
    double angleHorizontal;  // degree
    if (holder.isAimingLeft()) {
      angleHorizontal = (mouseSubGrip.angleBetween(Vector2.Left())) * 180 / PI;
      if (angleHorizontal > aimAngleMax) {
        holder.setAimingLeft(false);
        angle = angleHorizontal - 180f;
      }
      if (angleHorizontal > 90f) {
        angle = angleHorizontal * (mouseY > this.getGripY() ? -1 : 1);
      }
    } else { // holder aiming Right
      angleHorizontal = (mouseSubGrip.angleBetween(Vector2.Right())) * 180 / PI;
      if (angleHorizontal > aimAngleMax) {
        holder.setAimingLeft(true);
        angle = 180f - angleHorizontal;
      }
      if (angleHorizontal > 90f) {
        angle = angleHorizontal * (mouseY > this.getGripY() ? 1 : -1);
      }
    }

    angleRadian = angle * PI / 180;

    // Rotate and translate the image
    if (holder.isAimingLeft()) {
      imageView.setScaleX(-1);
      rotate.setAngle(-angle);
      imageView.getTransforms().add(rotate);
      imageView.setTranslateX(this.getGripFlipX());
      imageView.setTranslateY(this.getGripFlipY());

      holder.setHandLeftX(this.getForeGripFlipX());
      holder.setHandLeftY(this.getForeGripFlipY());
    } else {
      imageView.setScaleX(1);
      rotate.setAngle(angle);
      imageView.getTransforms().add(rotate);
      imageView.setTranslateX(this.getGripX());
      imageView.setTranslateY(this.getGripY());

      holder.setHandRightX(this.getForeGripX());
      holder.setHandRightY(this.getForeGripY());
    }
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  public double getBulletSpeed() {
    return this.bulletSpeed;
  }

  public void setBulletSpeed(double newSpeed) {
    if (newSpeed > 0 && newSpeed < 50.0) {
      this.bulletSpeed = newSpeed;
    }
  }

  public double getBulletWidth() {
    return this.bulletWidth;
  }

  public void setBulletWidth(double newWidth) {
    if (newWidth > 0) {
      this.bulletWidth = newWidth;
    }
  }

  public boolean isFullAutoFire() {
    return this.fullAutoFire;
  }

  public boolean isSingleHanded() {
    return this.singleHanded;
  }

  // -------------------
  // Setters and Getters
  // --------END--------
}
