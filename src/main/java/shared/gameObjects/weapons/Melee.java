package shared.gameObjects.weapons;

import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public abstract class Melee extends Weapon {

  protected double range; // radius in pixels
  protected double beginAngle; // swing from beginAngle (relative to arm)
  protected double endAngle; // swing till endAngle (relative to arm)
  protected Rigidbody rb;
  protected boolean attacking;
  protected double[] angles;
  protected int currentAngleIndex;

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
      Player holder,
      double range,
      double beginAngle,
      double endAngle,
      UUID uuid) {

    super(x, y, sizeX, sizeY, id, damage, weight, name, false, true, 10, fireRate, holder, uuid);
    this.range = range;
    this.beginAngle = beginAngle;
    this.endAngle = endAngle;
    this.angles = generateAngles();
    this.attacking = false;
    this.currentAngleIndex = 0;

    addComponent(new CircleCollider(this, (float) range, false));
    rb = new Rigidbody(
        RigidbodyType.STATIC,
        50f,
        0,
        0.1f,
        new MaterialProperty(0, 1, 1),
        new AngularData(0, 0, 0, 0),
        this);//TODO FIX
    addComponent(rb);
  }

  @Override
  public void update() {
    deductCooldown();
    super.update();
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      this.attacking = true;
      UUID uuid = UUID.randomUUID();
      // TODO: add circle cast
      // maybe use box cast?
      // ArrayList<Collision> collisions = Physics.circlecastAll(sourcePos, distance);
      ArrayList<Collision> collisions = Physics.boxcastAll(
          new Vector2((float) (this.getX() + this.range), (float) (this.getY() - this.range)),
          new Vector2((float) this.range, (float) this.range));
      ArrayList<Player> playersBeingHit = new ArrayList<>();

      //System.out.println("=============start==============");
      for (Collision c : collisions) {
        GameObject g = c.getCollidedObject().getParent();
        if (g.getId() == ObjectID.Player && !g.equals(holder)) {
          System.out.print(g.toString() + " -> ");
          //System.out.println(((Player) g).getHealth());
          playersBeingHit.add((Player) g);
        }
      }
      //System.out.println("================end===========");
      
      
      this.currentCooldown = getDefaultCoolDown();

      for (Player p : playersBeingHit) {
        p.deductHp(this.damage);
      }
      
      deductAmmo();
    }
  }

  private double[] generateAngles() {
    double[] angle = new double[(int) (beginAngle + endAngle) + 1];
    int k = 0;
    for (double i = beginAngle; i >= (endAngle * -1); i--) {
      angle[k] = i;
      ++k;
    }

    return angle;
  }

  // -------------------
  // Setters and Getters
  // -------------------
  public double getAngle(int index) {
    if (index < (int) (beginAngle + endAngle + 1)) {
      return angles[index];
    }
    return 0;
  }
  
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
