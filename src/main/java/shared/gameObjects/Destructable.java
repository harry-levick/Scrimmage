package shared.gameObjects;

/**
 * Interface for objects that can be damaged or destroyed via damage
 */
public interface Destructable {

  /**
   * Deals damage to the object; the nature of the damage is set by the object.
   *
   * @param damage The amount of proposed damage to deal to the hp
   */
  void deductHp(int damage);
}
