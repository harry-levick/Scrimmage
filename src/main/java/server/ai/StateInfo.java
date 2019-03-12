package server.ai;

import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Melee;

/**
 * A Utility class
 */
public class StateInfo {

  protected static Melee tempMelee;
  protected static double weaponRange;
  protected static int ammoLeft;
  protected static int botHealth;

  /**
   * Gets the info needed by the automata.
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
