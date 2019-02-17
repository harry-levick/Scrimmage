package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.CircleCollider;
import shared.physics.Physics;

public abstract class Melee extends Weapon {

  protected double range; // radius in pixels
  protected double beginAngle; // swing from beginAngle (relative to arm)
  protected double endAngle; // swing till endAngle (relative to arm)

  public Melee(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectID id,
      int damage,
      double weight,
      String name,
      int ammo,
      int fireRate,
      double range,
      double beginAngle,
      double endAngle,
      UUID uuid) {

    super(x, y, sizeX, sizeY, id, damage, weight, name, false, true, 10, fireRate, uuid);
    this.range = range;
    this.beginAngle = beginAngle;
    this.endAngle = endAngle;
  }

  @Override
  public void update() {
    deductCooldown();
    super.update();
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      // TODO: add circle cast
      // maybe use box cast?
      // ArrayList<Collision> collisions = Physics.circlecastAll(sourcePos, distance);
      this.currentCooldown = getDefaultCoolDown();
      deductAmmo();
    }
  }

  // -------------------
  // Setters and Getters
  // -------------------
  public double getRange() {
    return this.range;
  }

  public void setRange(double newRange) {
    if (newRange > 0 && newRange < 100.0) {
      this.range = newRange;
    }
  }

  public double getBeginAngle() {
    return this.beginAngle;
  }

  public void setBeginAngle(double newBeginAngle) {
    if (newBeginAngle > 0 && newBeginAngle < 90.0) {
      this.beginAngle = newBeginAngle;
    }
  }

  public double getEndAngle() {
    return this.endAngle;
  }

  public void setEndAngle(double newEndAngle) {
    if (newEndAngle > 0 && newEndAngle < 90.0) {
      this.endAngle = newEndAngle;
    }
  }
}
