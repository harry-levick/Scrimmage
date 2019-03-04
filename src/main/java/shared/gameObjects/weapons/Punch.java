package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;

/**
 * Default holding weapon of a player
 */
public class Punch extends Melee {

  /**
   * Constructor for Punch
   *
   * @param x x position of player's hand
   * @param y y position of player's hand
   * @param id ObjectType
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
      ObjectType id,
      int damage,
      String name,
      Player holder,
      double range,
      UUID uuid) {
    super(x, y, sizeX, sizeY, id, damage, 1, name, -1, 60, holder, range,
        1, 1, uuid);
  }

  public Punch(Punch that) {
    this(that.getX(), that.getY(), that.sizeX, that.sizeY, that.id, that.damage, that.name,
        that.holder, that.range, that.objectUUID);
  }

  @Override
  public void initialiseAnimation() {

  }
}
