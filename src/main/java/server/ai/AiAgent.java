package server.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import server.ai.pathFind.AStar;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

/** @author Harry Levick (hxl799) */

/**
 * AiAgent is the main body of an ai, creating an AiAgent will create a bot in the world at (x,y).
 * The AiAgent class then has the main loop of the bot, which is inside the startAgent() method,
 * calling this method begins the main loop.
 * Before the startAgent() method is called, you must call the getBot() method so that the bot can
 * be added to the list of gameObjects.
 */
public class AiAgent {

  Bot bot;
  FSA state;
  boolean active;
  ArrayList<GameObject> gameObjects;
  Player targetPlayer;
  AStar pathFinder;

  public AiAgent(double xPos, double yPos, ObjectID id, UUID uuid,
      ArrayList<GameObject> gameObjects) {
    this.bot = new Bot(xPos, yPos, id, uuid);
    this.state = FSA.INITIAL_STATE;
    this.active = false;
    this.gameObjects = gameObjects;
    this.pathFinder = new AStar(gameObjects, this.bot);
  }

  /**
   * The method that runs the agent.
   */
  public void startAgent() {
    active = true;
    double prevDist, newDist;
    /**
     * Would I need to fetch all players on each loop, or would a single fetch outside of the loop
     * give a reference to all players that updates with the player updates?
     */
    // Collect all players from the world
    List<Player> allPlayers = gameObjects.stream()
        .filter(p -> p instanceof Player)
        .map(Player.class::cast)
        .collect(Collectors.toList());

    // Update the targeted player
    targetPlayer = findTarget(allPlayers);

    while (active) {
      /**
       * The ai can be in one of 6 states at any one time.
       * The state it is in determines the actions that it takes.
       */
      switch (state) {
        case IDLE:
          // TODO what to do in the idle state?
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
        /**
         * The ai will always be in the initial state on the first loop, so will default
         * allowing us to find the target player for the first time in the default case.
         */
        default:
          // Calculate the distance to the target from the previous loop
          prevDist = calcDistance(bot, targetPlayer);
          // Update the target player
          targetPlayer = findTarget(allPlayers);
          // Calculate the distance to the updated target
          newDist = calcDistance(bot, targetPlayer);

          state = state.next(targetPlayer, bot, prevDist, newDist);
      }
    }

  }

  /**
   * Receives an action and then executes this action.
   * This method will only execute one action at a time (the first action in the list). Since the
   * method will be called inside of the agent loop
   * @param actions: a list of actions to take.
   */
  private void executeAction(ArrayList<boolean[]> actions) {
    // TODO decide on the implementation of action execution.
    bot.jumpKey = true;
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
