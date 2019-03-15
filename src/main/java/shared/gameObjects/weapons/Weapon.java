package shared.gameObjects.weapons;

import java.util.UUID;
import javafx.scene.transform.Rotate;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;

/**
 * The abstract class of all weapons in the game.
 */
public abstract class Weapon extends GameObject {

  /**
   * Cooldown of a weapon = MAX_COOLDOWN - fireRate.
   * For every frame, deduct cooldown by 1.
   * If cooldown == 0, the weapon can be fired. Otherwise, nothing will happen when mouse is
   * left-clicked
   */
  protected int MAX_COOLDOWN = 81;
  /** Constant value PI */
  protected float PI = 3.141592654f;
  /** Weight of the weapon */
  protected double weight; // grams
  /** Name of the weapon */
  protected String name; // name of the weapon
  /** True if this is a gun */
  protected boolean isGun;
  /** True if this is a melee */
  protected boolean isMelee;
  /** Ammo of the weapon, -1 for unlimited ammo */
  protected int ammo;
  /** Fire rate of the weapon, max = MAX_COOLDOWN - 1 */
  protected int fireRate;
  /** Current cooldown of the weapon, ready to fire when equals 0 */
  protected int currentCooldown;

  /** The player who holds the weapon, null if none */
  protected Player holder;
  /**
   * The hand position of the holder represented as an array.
   * array[0] = x position of hand
   * array[1] = y position of hand
   * The values change based on the aiming direction of the weapon.
   * There is value only when holder != null
   */
  protected double[] holderHandPos;

  /**
   * Angle of aiming in radian measured about x axis
   *      |
   *   +  |  -
   * -----------
   *   -  |  +
   *      |
   */
  protected double angleRadian;
  /**
   * Rotate property of imageView with
   * pivot(x) = 20  TODO: change here after changing pivot of guns
   * pivot(y) = 10
   */
  protected transient Rotate rotate;

  // variables for when the holder is null
  private BoxCollider bcTrig;
  private BoxCollider bcCol;
  private Rigidbody rb;

  /**
   * Constructor of the weapon class
   *
   * @param x X position of this weapon
   * @param y Y position of this weapon
   * @param sizeX Horizontal size of the image
   * @param sizeY Vertical size of the image
   * @param id ObjectType of this weapon
   * @param weight Weight of this weapon
   * @param name Name of this weapon
   * @param isGun True if this weapon is a gun
   * @param isMelee True if this weapon is a melee
   * @param ammo Ammo of this weapon (> 0)
   * @param fireRate FireRate of this weapon
   * @param holder Player who holds this weapon
   * @param uuid UUID of this weapon
   */
  public Weapon(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectType id,
      double weight,
      String name,
      boolean isGun,
      boolean isMelee,
      int ammo,
      int fireRate,
      Player holder,
      UUID uuid) {
    super(x, y, sizeX, sizeY, id, uuid);
    this.isGun = isGun;
    this.isMelee = isMelee;
    setWeight(weight);
    this.name = name;
    setAmmo(ammo);
    setFireRate(fireRate);
    this.holder = holder;
    holderHandPos = new double[]{};

    if (holder == null) {
      // add collider and rigidbody
      bcTrig = new BoxCollider(this, ColliderLayer.DEFAULT, true);
      bcCol = new BoxCollider(this, ColliderLayer.COLLECTABLE, false);
      rb = new Rigidbody(
          RigidbodyType.DYNAMIC,
          1f, // mass
          1f, // gravity scale
          0.1f, // air drag
          new MaterialProperty(0.1f, 1, 1),
          new AngularData(0, 0, 0, 0),
          this); // TODO FIX
      addComponent(bcCol);
      addComponent(bcTrig);
      addComponent(rb);
    }

    this.currentCooldown = 0;
  }

  /**
   * Fire a weapon, to be implemented on each weapon children
   *
   * @param mouseX Position of mouse x position when clicked
   * @param mouseY Position of mouse y position when clicked
   */
  public abstract void fire(double mouseX, double mouseY);

  /** Get the x position of the grip */
  public abstract double getGripX();

  /** Get the y position of the grip */
  public abstract double getGripY();

  /** Get the x position of the grip when aiming the left hand side */
  public abstract double getGripFlipX();

  /** Get the y position of the grip when aiming the left hand side */
  public abstract double getGripFlipY();

  @Override
  public void update() {
    super.update();
    if (holder != null) {
      holderHandPos = getHolderHandPos();
      this.setX(getGripX());
      this.setY(getGripY());
    }
  }

  /**
   * Get holder hand position
   *
   * @return A double[] with the first element being the x position and the second
   *         element being the y position, or null if there is no holder (i.e. holder==null)
   */
  public double[] getHolderHandPos() {
    if (holder != null) {
      if (this.isGun) {
        return holder.getGunHandPos();
      } else  // isMelee
      {
        return holder.getMeleeHandPos();
      }
    }
    return null;
  }

  /**
   * Get the default cooldown of a weapon
   *
   * @return Default cooldown (MAX_COOLDOWN - fireRate)
   */
  public int getDefaultCoolDown() {
    return MAX_COOLDOWN - this.fireRate;
  }

  /**
   * Deduct the cooldown of the weapon by 1 if the current cooldown > 0
   */
  public void deductCooldown() {
    if (this.currentCooldown > 0) {
      this.currentCooldown -= 1;
    }
  }

  /**
   * Deduct the ammo of the weapon by 1 if the current ammo > 0
   */
  public void deductAmmo() {
    if (this.ammo > 0) {
      this.ammo -= 1;
    }
  }

  /**
   * Check if the weapon can be fired by seeing if currentCooldown <= 0
   *
   * @return True if the weapon can fire, false otherwise
   */
  public boolean canFire() {
    return this.currentCooldown <= 0;
  }

  /**
   * Destroy the weapon by removing it from update loop, remove the rendering
   * and set active to false
   */
  public void destroyWeapon() {
    settings.getLevelHandler().removeGameObject(this);
  }

  @Override
  public void OnTriggerEnter(Collision col) {
    GameObject g = col.getCollidedObject();
    if (g != null && g.getId() == ObjectType.Player && ((Player) g).getHolding() == null) {
      Player p = (Player) g;
      setHolder(p);
      bcCol.setLayer(ColliderLayer.PARTICLE);
      bcTrig.setLayer(ColliderLayer.PARTICLE);
      this.removeComponent(rb);
    }
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  /** Get the current cooldown */
  public int getCoolDown() {
    return this.currentCooldown;
  }

  /** Get the weight */
  public double getWeight() {
    return this.weight;
  }

  /** Set a new weight, with value between 0 to 1000f exclusive */
  public void setWeight(double newWeight) {
    if (newWeight > 0 && newWeight < 1000.0f) {
      this.weight = newWeight;
    }
  }

  /**
   * See if the current weapon is a gun or not
   */
  public boolean isGun() {
    return isGun;
  }

  /**
   * See if the current weapon is a melee or not
   */
  public boolean isMelee() {
    return isMelee;
  }

  /** Get the name of the weapon */
  public String getName() {
    return this.name;
  }

  /** Get the ammo of the weapon */
  public int getAmmo() {
    return this.ammo;
  }

  /**
   * Set a new ammo to the weapon
   *
   * @param newAmmo New ammo, integer > 0 or -1 for unlimited
   */
  public void setAmmo(int newAmmo) {
    if (newAmmo == -1 || newAmmo > 0) {
      this.ammo = newAmmo;
    }
  }

  public int getFireRate() {
    return this.fireRate;
  }

  /**
   * Set new fire rate of the wepaon
   *
   * @param newFireRate New fire rate, integer > 0
   */
  public void setFireRate(int newFireRate) {
    if (newFireRate > 0) {
      this.fireRate = newFireRate;
    }
  }

  /** Get the holder of the weapon
   *
   * @return A Player or null if there is no holder
   */
  public Player getHolder() {
    return this.holder;
  }

  /**
   * Set holder of this weapon
   *
   * @param p New holder of the weapon, null not accepted
   */
  public void setHolder(Player p) {
    if (p != null) {
      this.holder = p;
      p.setHolding(this);
    }
  }
  // -------------------
  // Setters and Getters
  // --------END--------

}
