package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;

/**
 * Default holding weapon of a player
 */
public class Punch extends Melee {

  /**
   * Constructor for Punch
   *
   * @param x x position of player's hand
   * @param y y position of player's hand
   * @param id ObjectID
   * @param damage Damage of a punch
   * @param name Name
   * @param range Range of punch, measured forward
   * @param uuid UUID
   */
  public Punch(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectID id,
      double damage,
      String name,
      double range,
      UUID uuid) {
    super(x, y, sizeX, sizeY, id, damage, 1, name, -1, 60, range, 1, 1, uuid);
  }

  @Override
  public void initialiseAnimation() {
    // TODO Auto-generated method stub

  }

  @Override
  public String getState() {
    // TODO Auto-generated method stub
    return null;
  }
}
