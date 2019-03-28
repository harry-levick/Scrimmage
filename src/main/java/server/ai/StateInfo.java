package server.ai;

import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Melee;

/**
 * A Utility class
 */
public class StateInfo {

  /**
   * The weapon used for the bot to get range
   */
  protected static Melee tempMelee;
  /**
   * The range of the the bots melee weapon if they have one
   */
  protected static double weaponRange;
  /**
   * The ammo that the bots weapon has
   */
  protected static int ammoLeft;
  /**
   * The health of the bot
   */
  protected static int botHealth;

  /**
   * Gets the info needed by the automata.
   *
   * @param target The target of the bot
   * @param bot The bot
   */
  protected static void setInfo(Player target, Player bot) {
    weaponRange =
        (bot.getHolding().isGun())
            ? Double.POSITIVE_INFINITY
            : (tempMelee = (Melee) bot.getHolding()).getRange();
    ammoLeft = (bot.getHolding().isGun()) ? bot.getHolding().getAmmo() : 0;

    botHealth = bot.getHealth();
  }
}
