package shared.gameObjects.weapons;

import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.maths.Vector2;

/**
 * The abstract class for all guns type weapon.
 */
public abstract class Gun extends Weapon {

  /** Maximum angle of aiming before switching holding hand */
  protected float AIM_ANGLE_MAX = 110f;

  /** True if this gun is full auto fire */
  protected boolean fullAutoFire;

  /**
   * Rotation angle of the Gun
   */
  private double angle;

  /**
   * Constructor of the Gun class
   *
   * @param x The x position of the gun
   * @param y The y position of the gun
   * @param sizeX Horizontal size of the image
   * @param sizeY Vertical size of the image
   * @param weight Weight of the gun
   * @param name Name of the gun
   * @param ammo Total amount of ammo
   * @param fireRate Fire rate of the gun (bullets per minute)
   * @param holder Player that holds this gun
   * @param fullAutoFire True if this gun is full auto fire
   * @param singleHanded True if this gun is held with single hand
   * @param uuid UUID of this gun
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
      double pivotX,
      double pivotY,
      Player holder,
      boolean fullAutoFire,
      boolean singleHanded,
      UUID uuid) {

    super(x, y, sizeX, sizeY, ObjectType.Weapon, weight, name, true, false, ammo, fireRate,
        pivotX, pivotY, holder, uuid);

    this.fullAutoFire = fullAutoFire;
    this.singleHanded = singleHanded;
    this.angle = 0;
  }

  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    this.angle = 0;
  }

  /**
   * Contains the state of the object for sending over server Only contains items that need sending
   * separate by commas
   *
   * @return State of object
   */
  @Override
  public String getState() {
    return objectUUID + ";" + id + ";" + (float) getX() + ";" + (float) getY() + ";"
        + (float) angle;
  }

  @Override
  public void setState(String data, Boolean snap) {
    String[] unpackedData = data.split(";");
    setX(Double.parseDouble(unpackedData[2]));
    setY(Double.parseDouble(unpackedData[3]));
    this.angle = Double.parseDouble(unpackedData[4]);
  }

  public abstract double getForeGripX();

  public abstract double getForeGripY();

  public abstract double getForeGripFlipX();

  public abstract double getForeGripFlipY();

  public abstract boolean firesExplosive();

  @Override
  public void update() {
    deductCooldown();
    super.update();
    if (startedThrowing || holder == null) {
      return;
    }

    double mouseX = holder.mouseX;
    double mouseY = holder.mouseY;
    Vector2 mouseV = new Vector2((float) mouseX, (float) mouseY);
    Vector2 gripV = new Vector2((float) this.getGripX(), (float) this.getGripY());
    Vector2 mouseSubGrip = mouseV.sub(gripV);
    angleRadian = mouseSubGrip.normalize().angleBetween(Vector2.Zero());  // radian
    angle = angleRadian * 180 / PI;  // degree

    // Change the facing of the player when aiming the other way
    double angleHorizontal;  // degree
    if (holder.isAimingLeft()) {
      angleHorizontal = (mouseSubGrip.angleBetween(Vector2.Left())) * 180 / PI;
      if (angleHorizontal > AIM_ANGLE_MAX) {
        holder.setAimingLeft(false);
        angle = angleHorizontal - 180f;
      }
      if (angleHorizontal > 90f) {
        angle = angleHorizontal * (mouseY > this.getGripY() ? -1 : 1);
      }
    } else { // holder aiming Right
      angleHorizontal = (mouseSubGrip.angleBetween(Vector2.Right())) * 180 / PI;
      if (angleHorizontal > AIM_ANGLE_MAX) {
        holder.setAimingLeft(true);
        angle = 180f - angleHorizontal;
      }
      if (angleHorizontal > 90f) {
        angle = angleHorizontal * (mouseY > this.getGripY() ? 1 : -1);
      }
    }
    angleRadian = angle * PI / 180;
  }

  @Override
  public void render() {
    super.render();
    if (!(startedThrowing || holder == null)) {
      imageView.getTransforms().clear();
      // Rotate and translate the image
      if (holder.isAimingLeft()) {
        imageView.setScaleX(-1);
        rotate.setAngle(-angle);
        imageView.getTransforms().add(rotate);
        imageView.setTranslateX(this.getGripFlipX());
        imageView.setTranslateY(this.getGripFlipY());

        //holder.setHandLeftX(this.getForeGripFlipX());
        //holder.setHandLeftY(this.getForeGripFlipY());
      } else {
        imageView.setScaleX(1);
        rotate.setAngle(angle);
        imageView.getTransforms().add(rotate);
        imageView.setTranslateX(this.getGripX());
        imageView.setTranslateY(this.getGripY());

        //holder.setHandRightX(this.getForeGripX());
        // holder.setHandRightY(this.getForeGripY());
      }

    }
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  /** Returns true if this gun is full auto fire */
  public boolean isFullAutoFire() {
    return this.fullAutoFire;
  }

  /** Returns true if this gun is held with one hand */
  public boolean isSingleHanded() {
    return this.singleHanded;
  }
  // -------------------
  // Setters and Getters
  // --------END--------
}
