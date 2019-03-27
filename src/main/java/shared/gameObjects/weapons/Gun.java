package shared.gameObjects.weapons;

import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/**
 * The abstract class for all guns type weapon.
 */
public abstract class Gun extends Weapon {

  /** Segment of package name of blocks, to check if an object is instance of Blocks */
  protected static String blocksPackageName = ".Blocks.";

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

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    this.angle = 0;
  }

  /**
   * Check if the player is shooting at the floor, as there might be a chance to shoot through
   * the floor
   *
   * @param bulletX X position of bullet start position
   * @param bulletY Y position of bullet start position
   * @param mouseX X position of mouse
   * @param mouseY Y position of mouse
   * @param playerCentre Centre of player (main hand / centre of box collider)
   * @return Array double with 2 elements: X and Y position of bullet starting position
   */
  protected double[] isShootingFloor(
      double bulletX, double bulletY,
      double mouseX, double mouseY,
      Vector2 playerCentre) {

    Collision raycast = Physics.raycastBullet(
        playerCentre,
        new Vector2(mouseX - bulletX, mouseY - bulletY).normalize().mult(55),
        this.holder,
        false);

    // Raycast first collider is floor
    if (raycast != null && raycast.getCollidedObject().getClass().getPackage().getName()
        .contains(blocksPackageName)) {
      return new double[] { holderHandPos[0], holderHandPos[1] + 20 };
    } else { // Not floor
      return new double[] { bulletX, bulletY };
    }
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

  /**
   * Get the X position of the fore grip
   *
   * @return X position of the fore grip
   */
  public abstract double getForeGripX();

  /**
   * Get the Y position of the fore grip
   *
   * @return Y position of the fore grip
   */
  public abstract double getForeGripY();

  /**
   * Get the X position of the fore grip when the image is flipped
   *
   * @return X position of the flipped fore grip
   */
  public abstract double getForeGripFlipX();

  /**
   * Get the Y position of the fore grip when the image is flipped
   *
   * @return Y position of the flipped fore grip
   */
  public abstract double getForeGripFlipY();

  /**
   * Check if the gun fires explosive bullets
   *
   * @return True if the gun fires explosive bullets
   */
  public abstract boolean firesExplosive();

  @Override
  public void update() {
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
