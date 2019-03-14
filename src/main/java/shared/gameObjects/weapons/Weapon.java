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
   * Cooldown of a weapon = MAX_COOLDOWN - fireRate For every frame, deduct cooldown by 1 If
   * cooldown == 0, the weapon can be fired. Otherwise, nothing will happen when mouse is
   * left-clicked
   */
  protected int MAX_COOLDOWN = 81;
  protected float PI = 3.141592654f;

  protected double weight; // grams
  protected String name; // name of the weapon
  protected boolean isGun;
  protected boolean isMelee;
  protected int ammo; // -1 = unlimited
  protected int fireRate; // max = MAX_COOLDOWN - 1
  protected int currentCooldown;

  protected Player holder; // holder of the weapon
  protected double[] holderHandPos;

  protected double angleRadian; // angle of gun (hand and mouse vs x-axis) (radian)
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
   * @param id ObjectType of this weapon
   * @param weight Weight of this weapon
   * @param name Name of this weapon
   * @param isGun True if this weapon is a gun
   * @param isMelee True if this weapon is a melee
   * @param ammo Ammo of this weapon (> 0)
   * @param fireRate FireRate of this weapon
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
          0.1f,
          new MaterialProperty(0.1f, 1, 1),
          new AngularData(0, 0, 0, 0),
          this); // TODO FIX
      addComponent(bcCol);
      addComponent(bcTrig);
      addComponent(rb);
    }

    this.currentCooldown = 0;
  }

  public abstract void fire(double mouseX, double mouseY);

  public abstract double getGripX();

  public abstract double getGripY();

  public abstract double getGripFlipX();

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

  // Get holder hand position
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

  public int getDefaultCoolDown() {
    return MAX_COOLDOWN - this.fireRate;
  }

  public void deductCooldown() {
    if (this.currentCooldown > 0) {
      this.currentCooldown -= 1;
    }
  }

  public void deductAmmo() {
    if (this.ammo > 0) {
      this.ammo -= 1;
    }
  }

  public boolean canFire() {
    return this.currentCooldown <= 0;
  }

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
  public int getCoolDown() {
    return this.currentCooldown;
  }

  public double getWeight() {
    return this.weight;
  }

  public void setWeight(double newWeight) {
    if (newWeight > 0 && newWeight < 1000.0f) {
      this.weight = newWeight;
    }
  }

  public boolean isGun() {
    return isGun;
  }

  public void setIsGun(boolean gun) {
    isGun = gun;
  }

  public boolean isMelee() {
    return isMelee;
  }

  public void setIsMelee(boolean melee) {
    isMelee = melee;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public int getAmmo() {
    return this.ammo;
  }

  public void setAmmo(int newAmmo) {
    if (newAmmo == -1 || newAmmo > 0) {
      this.ammo = newAmmo;
    }
  }

  public int getFireRate() {
    return this.fireRate;
  }

  public void setFireRate(int newFireRate) {
    if (newFireRate > 0) {
      this.fireRate = newFireRate;
    }
  }

  public Player getHolder() {
    return this.holder;
  }

  //Set holder of this gun
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
