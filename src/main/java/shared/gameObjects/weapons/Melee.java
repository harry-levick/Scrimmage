package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;

public abstract class Melee extends Weapon {

  double range; // radius in pixels
  double beginAngle; // swing from beginAngle (relative to arm)
  double endAngle; // swing till endAngle (relative to arm)

  public Melee(
      double x,
      double y,
      ObjectID id,
      double _damage,
      double _weight,
      String _name,
      double _range,
      double _beginAngle,
      double _endAngle,
      UUID uuid) {

    super(x, y, id, _damage, _weight, _name, uuid);
    setRange(_range);
    setBeginAngle(_beginAngle);
    setEndAngle(_endAngle);
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
