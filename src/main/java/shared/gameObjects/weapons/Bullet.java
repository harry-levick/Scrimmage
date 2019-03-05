package shared.gameObjects.weapons;

import client.main.Client;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.transform.Rotate;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * @author hlf764
 */
public abstract class Bullet extends GameObject {

  private double PI = 3.141592654;

  public boolean isHit; // true if there is an object at that position
  protected Rigidbody rb;
  private double width; // width of bullet
  private double speed; // speed of bullet
  private Vector2 vector; // Vector of the force of bullet fire
  private int damage; // Damage of this bullet
  private Player holder; // Holder of the gun that fired this bullet
  private Rotate rotate;

  public Bullet(
      double gunX, // gun initial x position
      double gunY, // gun initial y position
      double mouseX, // mouse initial x position
      double mouseY, // mouse initial y position
      double width, // the width of the bullet
      double speed, // the speed of the bullet
      int damage, // damage of this bullet
      Player holder, // holder of the gun that fired this bullet
      UUID uuid) { // uuid of this bullet

    super(gunX, gunY, width, width, ObjectType.Bullet, uuid);
    setWidth(width);
    setSpeed(speed);
    this.damage = damage;
    this.holder = holder;
    this.isHit = false;

    // Unit vector of the bullet force
    vector = new Vector2((float) (mouseX - gunX), (float) (mouseY - gunY));
    vector = vector.div((float) Math.sqrt(vector.dot(vector)));
    addComponent(new BoxCollider(this, ColliderLayer.PARTICLE, false));
    rb =
        new Rigidbody(
            RigidbodyType.DYNAMIC,
            0.75f, // mass
            0,
            0.1f,
            new MaterialProperty(0.1f, 1, 1),
            new AngularData(0, 0, 0, 0),
            this); // TODO FIX
    addComponent(rb);

    // Rotate property of the image
    rotate = new Rotate();
    Vector2 mouseV = new Vector2((float) mouseX, (float) mouseY);
    Vector2 gunV = new Vector2((float) gunX, (float) gunY);
    Double bulletAngle = (double) mouseV.sub(gunV).angle(); // radian
    double angleDegree = bulletAngle * 180 / PI; // degree
    if (mouseX < gunX) {
      angleDegree = angleDegree + 180;
    }
    rotate.setAngle(angleDegree);

    Client.levelHandler.addGameObject(this);
    imageView.getTransforms().add(rotate);

    render();
  }

  @Override
  public void update() {
    super.update();
    ArrayList<Collision> collision =
        Physics.boxcastAll(
            getTransform().getPos(),
            getTransform().getSize());
    ArrayList<Player> playersBeingHit = new ArrayList<>();

    // check if a player is hit
    for (Collision c : collision) {
      GameObject g = c.getCollidedObject();
      if (g.getId() == ObjectType.Player && !g.equals(holder)) {
        isHit = true;
        playersBeingHit.add((Player) g);
      } else if (!g.equals(holder)) {
        isHit = true;
      }
    }

    if (isHit) {
      Client.levelHandler.removeGameObject(this);
      for (Player p : playersBeingHit) {
        p.deductHp(this.damage);
        // ((Rigidbody) p.getComponent(ComponentType.RIGIDBODY)).moveX((float) speed/10f);
      }
    } else if ((0 < getX() && getX() < 1920) && (0 < getY() && getY() < 1080)) {
      rb.move(vector.mult((float) speed));
    } else {
      Client.levelHandler.removeGameObject(this);
    }
  }

  @Override
  public void render() {
    super.render();
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
    getTransform()
        .scale(new Vector2((float) newWidth / (float) width, (float) newWidth / (float) width));
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
