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
     * give a reference to all players that updates with the player updates?
     */
    // Collect all players from the world
    List<Player> allPlayers = gameObjects.stream()
        .filter(p -> p instanceof Player)
        .map(Player.class::cast)
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
          // TODO what to do in the still state?
        case CHASING:
          // TODO calculate and execute the best path to the target.
        case FLEEING:
          // TODO calculate and execute the best path away from the target.
        case ATTACKING:
          // TODO think about how an attacking script would work.
        case CHASING_ATTACKING:
          // TODO calculate and execute the best path to the target whilst attacking.
        case FLEEING_ATTACKING:
          // TODO calculate and execute the best path away from the target whilst attacking.
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
    double targetDistance = Double.POSITIVE_INFINITY;

    for (Player p : allPlayers) {
      double distance = calcDistance(bot, p);
      // Update the target if another player is closer
      if (distance < targetDistance) {
        targetDistance = distance;
        target = p;
      }
    }

    return target;
  }

  /**
   * Calculate the distance between two players
   * @param p1 player 1
   * @param p2 player 2
   * @return the euclidean distance between p1 and p2
   */
  private double calcDistance(Player p1, Player p2) {
    double p1X, p1Y, p2X, p2Y;
    double changeX, changeY;
    p1X = p1.getX();
    p1Y = p1.getY();
    p2X = p2.getX();
    p2Y = p2.getY();

    changeX = p1X - p2X;
    changeY = p1Y - p2Y;

    return Math.sqrt(Math.pow(changeX, 2) + Math.pow(changeY, 2));
  }
}
