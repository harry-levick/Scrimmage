package shared.gameObjects.weapons;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.physics.Physics;

/**
 * Game object to handle weapon spawning in a map
 */
public class WeaponSpawner extends GameObject {

  private float timer;
  private static final float TIMER = 6.432f;
  private static final double WEAPON_PROBABILITY = 0.26;
  private ArrayList<Weapons> weapons;
  private Random random;
  private int guaranteedSpawn = 1;

  public WeaponSpawner(double x, double y, double sizeX, double sizeY, UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectType.WeaponSpawner, uuid);
    weapons =  new ArrayList<>();
    random = new Random();
    /**
     * Debugging
     */
    addWeaponToSpawner(
        Weapons.SWORD,
        Weapons.MACHINEGUN,
        Weapons.EXPLOSIVE_LAUNCHER,
        Weapons.UZI);
  }

  @Override
  public void initialiseAnimation() {
  //Has no image
    this.animation.supplyAnimation("default", "images/empty.png");
  }

  @Override
  public void update() {
    timer -= Physics.TIMESTEP;
    if(timer <= 0) {
      random = new Random();
      if(random.nextDouble() <= WEAPON_PROBABILITY) {
        createRandomWeapon();
      } else if (guaranteedSpawn > 0) {
        guaranteedSpawn--;
        createRandomWeapon();
      }
      timer = TIMER;
    }
  }

  /**
   * Adds a new weapon types to the spawner
   * @param weapon The weapon types to add to the spawner
   */
  public void addWeaponToSpawner(Weapons... weapon) {
    for (Weapons w : weapon) {
      if(!weapons.contains(w))
        weapons.add(w);
    }
  }

  private void createRandomWeapon() {

    int index = random.nextInt(weapons.size());
    switch (weapons.get(index)) {
      case SWORD:
        settings.getLevelHandler().addGameObject(new Sword(getX(), getY(), "SwordFromSpawner", null, UUID.randomUUID()));
        break;
      case HANDGUN:
        settings.getLevelHandler().addGameObject(new Handgun(getX(), getY(), "HandgunFromSpawner", null, UUID.randomUUID()));
        break;
      case MACHINEGUN:
        settings.getLevelHandler().addGameObject(new MachineGun(getX(), getY(), "MachinegunFromSpawner", null, UUID.randomUUID()));
        break;
      case UZI:
        settings.getLevelHandler().addGameObject(new Uzi(getX(), getY(), "UziFromSpawner", null, UUID.randomUUID()));
        break;
      case EXPLOSIVE_LAUNCHER:
        settings.getLevelHandler().addGameObject(new ExplosiveLauncher(getX(), getY(), "ExplosiveLauncherFromSpawner", null, UUID.randomUUID()));
        break;
    }
  }
}
