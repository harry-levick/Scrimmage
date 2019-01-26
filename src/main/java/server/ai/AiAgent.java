package server.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

/** @author Harry Levick (hxl799) */
public class AiAgent {

  Bot bot;
  FiniteStateAutomata state;
  boolean active;
  ArrayList<GameObject> gameObjects;
  Player targetPlayer;

  public AiAgent(double xPos, double yPos, ObjectID id, ArrayList<GameObject> gameObjects) {
    this.bot = new Bot(xPos, yPos, id);
    this.state = FiniteStateAutomata.INITIAL_STATE;
    this.active = false;
    this.gameObjects = gameObjects;
  }

  /**
   * The method that runs the agent.
   */
  public void startAgent() {
    active = true;
    /**
     * Would I need to fetch all players on each loop, or would a single fetch outside of the loop
     * give a reference to both players that updates with the player updates?
     */
    // Collect all players from the world
    List<Player> allPlayers = gameObjects.stream()
        .filter(p -> p instanceof Player)
        .collect(Collectors.toList());

    targetPlayer = findTarget(allPlayers);

    while (active) {
      state = state.next(targetPlayer, bot);

      /**
       * The ai can be in one of 6 states at any one time.
       * The state it is in determines the actions that it takes.
       */
      switch (state) {
        case STILL:

        case CHASING:

        case FLEEING:

        case ATTACKING:

        case CHASING_ATTACKING:

        case FLEEING_ATTACKING:

        default:
          // Update the targeted player
          targetPlayer = findTarget(allPlayers);
      }
    }

  }

  /**
   * Finds the closest player
   * @param allPlayers A list of all players in the world
   * @return The player who is the closest to the bot
   */
  private Player findTarget(List<Player> allPlayers) {
    Player target = null;
    double targetX, targetY, changeX, changeY;
    double botX = bot.getX();
    double botY = bot.getY();
    double targetDistance = Double.POSITIVE_INFINITY;

    for (Player p : allPlayers) {
      targetX = p.getX();
      targetY = p.getY();

      changeX = targetX - botX;
      changeY = targetY - botY;

      double distance = Math.sqrt(Math.pow(changeX, 2) + Math.pow(changeY, 2));
      // Update the target if another player is closer
      if (distance < targetDistance) {
        targetDistance = distance;
        target = p;
      }

    }
    
    return target;
  }
}
