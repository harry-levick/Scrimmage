package shared.gameObjects.weapons;

import client.main.Client;
import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * @author hlf764
 */
public abstract class Bullet extends GameObject {

  public boolean isHit;     // true if there is an object at that position
  protected Rigidbody rb;
  private double width;     // width of bullet
  private double speed;     // speed of bullet
  private Vector2 vector;   // Vector of the force of bullet fire
  private int damage;       // Damage of this bullet
  private Player holder;    // Holder of the gun that fired this bullet

  public Bullet(
      double gunX, // gun initial x position
      double gunY, // gun initial y position
      double mouseX, // mouse initial x position
      double mouseY, // mouse initial y position
      double width, // the width of the bullet
      double speed, // the speed of the bullet
      int damage,  // damage of this bullet
      Player holder, // holder of the gun that fired this bullet
      UUID uuid) { // uuid of this bullet

    super(gunX, gunY, 20, 20, ObjectID.Bullet, uuid);
    setWidth(width);
    setSpeed(speed);
    this.damage = damage;
    this.holder = holder;

    // Unit vector of the bullet force
    vector = new Vector2((float) (mouseX - gunX), (float) (mouseY - gunY));
    vector = vector.div((float) Math.sqrt(vector.dot(vector)));
    addComponent(new BoxCollider(this, false));
    rb = new Rigidbody(
        RigidbodyType.DYNAMIC,
        1f,  // mass
        0,
        0.1f,
        new MaterialProperty(0.1f, 1, 1),
        new AngularData(0, 0, 0, 0),
        this);//TODO FIX
    addComponent(rb);

    this.isHit = false;

    Client.levelHandler.addGameObject(this);

    render();
  }


  @Override
  public void update() {
    ArrayList<Collision> collision = Physics.boxcastAll(
        new Vector2((float) getX(), (float) getY()),
        new Vector2((float) this.width, (float) this.width));
    ArrayList<Player> playersBeingHit = new ArrayList<>();

    // check if a player is hit
    for (Collision c : collision) {
      GameObject g = c.getCollidedObject().getParent();
      if (g.getId() == ObjectID.Player && !g.equals(holder)) {
        isHit = true;
        playersBeingHit.add((Player) g);
        ((Rigidbody) holder.getComponent(ComponentType.RIGIDBODY)).move(new Vector2(-300,-300), 0.6f);
      }
    }
    
    if (isHit) {
      Client.levelHandler.delGameObject(this);
      for (Player p : playersBeingHit) {
        p.deductHp(this.damage);
      }
    } else if ((0 < getX() && getX() < 1920) && (0 < getY() && getY() < 1080)) {
      rb.move(vector.mult((float) speed));
      super.update();
    }
    else {
      Client.levelHandler.delGameObject(this);
    }
  }

  @Override
  public void render() {
    super.render();
    imageView.relocate(getX(), getY());
  }

  @Override
  public void interpolatePosition(float alpha) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getState() {
    return null;
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  public double getWidth() {
    return this.width;
  }

  public void setWidth(double newWidth) {
    if (newWidth > 0) {
      this.width = newWidth;
    }
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setSpeed(double newSpeed) {
    if (newSpeed > 0) {
      this.speed = newSpeed;
    }
  }

  public boolean getIsHit() {
    return this.isHit;
  }

  public void setIsHit(boolean hit) {
    this.isHit = hit;
  }
  // -------------------
  // Setters and Getters
  // --------END--------

}
