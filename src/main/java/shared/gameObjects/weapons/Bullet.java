package shared.gameObjects.weapons;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.emitters.CircleEmitter;
import client.main.Settings;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Limbs.Hand;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * Abstract class of Bullet objects
 */
public abstract class Bullet extends GameObject {

  /**
   * True if this bullet collided with some other objects
   */
  public boolean isHit;
  /**
   * Box Collider of this bullet
   */
  protected BoxCollider bc;
  /**
   * Rigidbody of this bullet
   */
  protected Rigidbody rb;
  /**
   * Holder of the gun that fired this bullet
   */
  protected Player holder;
  /**
   * The BoxCollider of the holder
   */
  protected Component holderBoxCollider;
  /**
   * true if it hit the holder (For OnCollisionExit)
   */
  protected boolean hitHolder;

  /** Constant value PI */
  private double PI = 3.141592654;
  /** Width of the bullet */
  private double width;
  /** Speed of travel of the bullet */
  private double speed;
  /** Vector of the force of bullet fire */
  private Vector2 vector;
  /** Damage of the bullet */
  private int damage;
  /** Angle of firing in degree */
  private double angleDegree;
  private HashSet<GameObject> alreadyHit;

  /**
   * Constructor of Bullet
   *
   * @param gunX X position of the gun
   * @param gunY Y position of the gun
   * @param mouseX X position of the mouse when this bullet is created
   * @param mouseY Y position of the mouse when this bullet is created
   * @param width Width of this bullet
   * @param speed Speed of travel of this bullet
   * @param damage Damage of this bullet
   * @param holder Player who fired this bullet
   * @param uuid UUID of this bullet
   */
  public Bullet(
      double gunX, // gun initial x position
      double gunY, // gun initial y position
      double mouseX, // mouse initial x position
      double mouseY, // mouse initial y position
      double width, // the width of the bullet
      double speed, // the speed of the bullet
      int damage, // hazard of this bullet
      Player holder, // holder of the gun that fired this bullet
      UUID uuid) { // uuid of this bullet

    super(gunX, gunY, width, width, ObjectType.Bullet, uuid);
    alreadyHit = new HashSet<>();
    setWidth(width);
    setSpeed(speed);
    this.damage = damage;
    this.holder = holder;
    this.isHit = false;

    // Unit vector of the bullet force
    vector = new Vector2((float) (mouseX - gunX), (float) (mouseY - gunY));
    vector = vector.div((float) Math.sqrt(vector.dot(vector)));
    bc = new BoxCollider(this, ColliderLayer.PROJECTILE, false);
    rb =
        new Rigidbody(
            RigidbodyType.DYNAMIC,
            0.75f, // mass
            0,
            0.1f,
            new MaterialProperty(0.1f, 1, 1),
            new AngularData(0, 0, 0, 0),
            this); // TODO FIX
    addComponent(bc);
    addComponent(rb);

    // Rotate property of the image
    Vector2 mouseV = new Vector2((float) mouseX, (float) mouseY);
    Vector2 gunV = new Vector2((float) gunX, (float) gunY);
    Double bulletAngle = (double) mouseV.sub(gunV).angle(); // radian
    angleDegree = bulletAngle * 180 / PI; // degree
    if (mouseX < gunX) {
      angleDegree = angleDegree + 180;
    }

    // Get the BoxCollider of the holder
    holderBoxCollider = holder.getComponent(ComponentType.COLLIDER);
    hitHolder = false;
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    // Rotate property of the image
    imageView.setRotate(angleDegree);
  }

  @Override
  public void update() {
    super.update();
    Random random = new Random();
    imageView.setRotate(angleDegree);
    if ((-200 < getX() && getX() < 1920) && (0 < getY() && getY() < 1080)) {
      rb.move(vector.mult((float) speed));
      settings
          .getLevelHandler()
          .addGameObject(
              new Particle(
                  transform.getPos(),
                  vector.mult((float) (-1f * speed * random.nextDouble() - 2)),
                  Vector2.Zero(),
                  new Vector2(12, 12).mult((float) random.nextDouble()),
                  "images/particle/BulletParticle.png",
                  0.2f));
    } else {
      settings.getLevelHandler().removeGameObject(this);
    }
  }

  @Override
  public void render() {
    super.render();
  }

  @Override
  public void OnCollisionEnter(Collision col) {
    boolean remove = true; // true: will remove this object at the end
    GameObject g = col.getCollidedObject();
    System.out.println(alreadyHit.size());
    if(!alreadyHit.contains(g)) {
      if (g instanceof Limb) {
        Limb p = (Limb) g;
        Limb q = p;
        if(p instanceof Hand) q = (Limb) q.getParent();
        if (q.getParent().equals(holder)) {
          remove = false;
          hitHolder = true;
        } else {
          p.deductHp(this.damage);
          alreadyHit.add(g);
          settings
              .getLevelHandler()
              .addGameObject(
                  new CircleEmitter(
                      col.getPointOfCollision(),
                      new Vector2(speed * 2, speed * 2),
                      new Vector2(0, Physics.GRAVITY*40),
                      new Vector2(6, 6),
                      bc.getSize().magnitude()/2,
                      0.34f,
                      Physics.TIMESTEP*2,
                      2,
                      false,
                      "images/particle/bloodParticle.png"));
        }
      }
    }
    // collision = player

    if (remove) {
      destroy();
      settings.getLevelHandler().removeGameObject(this);
    }
  }

  @Override
  public void OnCollisionStay(Collision col) {
    if (hitHolder) {
      removeComponent(holderBoxCollider);
    }
  }

  @Override
  public void OnCollisionExit(Collision col) {
    if (hitHolder) {
      addComponent(holderBoxCollider);
      hitHolder = false;
    }
  }

  @Override
  public void destroy() {
    if (!destroyed)
      settings
          .getLevelHandler()
          .addGameObject(
              new CircleEmitter(
                  bc.getCentre(),
                  new Vector2(speed * 5, speed * 5),
                  Vector2.Zero(),
                  new Vector2(12, 12),
                  bc.getSize().magnitude(),
                  0.34f,
                  Physics.TIMESTEP*2,
                  1,
                  false,
                  "images/particle/BulletParticle.png"));
    super.destroy();
  }

  // -------START-------
  // Setters and Getters
  // -------------------

  /**
   * Returns the width of this bullet
   */
  public double getWidth() {
    return this.width;
  }

  /**
   * Set a new width
   *
   * @param newWidth New width to set (positive double)
   */
  public void setWidth(double newWidth) {
    if (newWidth > 0) {
      this.width = newWidth;
    }
    getTransform()
        .scale(new Vector2((float) newWidth / (float) width, (float) newWidth / (float) width));
  }

  /**
   * Returns the speed of travel of this bullet
   */
  public double getSpeed() {
    return this.speed;
  }

  /**
   * Set a new speed of travel
   *
   * @param newSpeed New speed (positive double)
   */
  public void setSpeed(double newSpeed) {
    if (newSpeed > 0) {
      this.speed = newSpeed;
    }
  }

  /**
   * True if this bullet hit something
   */
  public boolean getIsHit() {
    return this.isHit;
  }

  /**
   * Set the status of hitting something
   */
  public void setIsHit(boolean hit) {
    this.isHit = hit;
  }
  // -------------------
  // Setters and Getters
  // --------END--------

}
