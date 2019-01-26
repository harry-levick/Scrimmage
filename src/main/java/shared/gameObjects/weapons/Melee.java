package shared.gameObjects.weapons;

import shared.gameObjects.Utils.ObjectID;

public abstract class Melee extends Weapon {

  protected double range; // radius in pixels
  protected double beginAngle; // swing from beginAngle (relative to arm)
  protected double endAngle; // swing till endAngle (relative to arm)

  public Melee(
      double x,
      double y,
      ObjectID id,
      double damage,
      double weight,
      String name,
      double range,
      double beginAngle,
      double endAngle
  ) {

    super(x, y, id, damage, weight, name, false, true);
    this.range = range;
    this.beginAngle = beginAngle;
    this.endAngle = endAngle;
  }

  // -------------------
  // Setters and Getters
  // -------------------

  public double getRange() {
    return this.range;
  }

  public void setRange(double newRange) {
    if (newRange > 0 && newRange < 100.0) this.range = newRange;
  }

  public double getBeginAngle() {
    return this.beginAngle;
  }

  public void setBeginAngle(double newBeginAngle) {
    if (newBeginAngle > 0 && newBeginAngle < 90.0) this.beginAngle = newBeginAngle;
  }

  public double getEndAngle() {
    return this.endAngle;
  }

  public void setEndAngle(double newEndAngle) {
    if (newEndAngle > 0 && newEndAngle < 90.0) this.endAngle = newEndAngle;
  }

  
}
