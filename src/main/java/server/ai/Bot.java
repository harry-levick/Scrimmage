package server.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import server.ai.pathFind.AStar;
import server.ai.pathFind.BotThread;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/** TODO : When the bot is created, create the bot on a new thread, and run the update method on
 *         a constant while loop.
 */

/**
 * @author Harry Levick (hxl799)
 */
public class Bot extends Player {

  public static final int KEY_JUMP = 0;
  public static final int KEY_LEFT = 1;
  public static final int KEY_RIGHT = 2;
  public static final int KEY_CLICK = 3;
  public float jumpTime;
  boolean mayJump = true;

  FSA state;
  public Player targetPlayer;
  AStar pathFinder;
  List<Player> allPlayers;
  // Get the LevelHandler through the constructor
  LevelHandler levelHandler;
  BotThread botThread;
  List<boolean[]> plan;

  /**
   *
   * @param x x pos of the bot
   * @param y y pos of the bot
   * @param playerUUID
   * @param levelHandler
   */
  public Bot(double x, double y, UUID playerUUID,
      LevelHandler levelHandler) {
    super(x, y, playerUUID);
    this.state = FSA.INITIAL_STATE;
    this.levelHandler = levelHandler;
    this.allPlayers = levelHandler.getPlayers();
    this.targetPlayer = findTarget();
    this.plan = Collections.synchronizedList(new ArrayList<>());
    this.botThread = new BotThread(this, plan);

  }

  public void startThread() {
    // Start the thread concurrently
    botThread.start();
  }

  public boolean mayJump() {
    return mayJump;
  }

  @Override
  public void update() {
    double prevDist, newDist;
    // Calculate the distance to the target from the previous loop
    prevDist = calcDist();
    // Calculate the distance to the updated target
    newDist = calcDist();
    targetPlayer = findTarget();

    state = state.next(targetPlayer, this, prevDist, newDist);

    switch (state) {
      case IDLE:
        System.out.println("IDLE");
        executeAction();

        break;
      case CHASING:
        System.out.println("CHASING");
        // Find the next best move to take, and execute this move.
        executeAction();

        break;
      case FLEEING:
        System.out.println("FLEEING");
        executeAction();
        // TODO calculate and execute the best path away from the target.

        break;
      case ATTACKING:
        System.out.println("ATTACKING");
        // TODO think about how an attacking script would work.
        Collision inSight = Physics.raycast(new Vector2((float) this.getX(), (float) this.getY()),
            new Vector2((float) targetPlayer.getX(), (float) targetPlayer.getY()));
        // If the target player is in sight of the bot, they can shoot.
        if (inSight == null) {
          mouseX = targetPlayer.getX();
          mouseY = targetPlayer.getY();
        }

        break;
      case CHASING_ATTACKING:
        System.out.println("CHASING-ATTACKING");
        //executeAction();
        // TODO calculate and execute the best path to the target whilst attacking.
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();

        break;
      case FLEEING_ATTACKING:
        System.out.println("CHASING-ATTACKING");
        executeAction();
        // TODO calculate and execute the best path away from the target whilst attacking.
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();

        break;
    }

    super.update();
  }

  /**
   * Calculates the distance from the bot to the current target player
   *
   * @return The distance to the target player
   */
  private double calcDist() {
    System.out.println("x = " + targetPlayer.getX() + ", y = " + targetPlayer.getY());
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(),
        (float) targetPlayer.getY());
    // Calculate the distance to the target
    return botPos.exactMagnitude(targetPos);
  }

  private void executeAction() {

    boolean[] action = new boolean[] {false, false, false, false};

    synchronized (plan) {
      if (plan.size() > 0)
        action = plan.remove(0);
    }

    Random r = new Random();
    // 60% chance of jumping when asked to.
    boolean jump = r.nextDouble() <= 0.60;
    if (jump) {
      this.jumpKey = action[Bot.KEY_JUMP];
    } else this.jumpKey = false;

    // 60% chance of moving left when asked to.
    boolean left = r.nextDouble() <= 0.60;
    if (left) {
      this.leftKey = action[Bot.KEY_LEFT];
    } else this.leftKey = false;

    // 60% chance of moving right when asked to
    boolean right = r.nextDouble() <= 0.60;
    if (right) {
      this.rightKey = action[Bot.KEY_RIGHT];
    } else this.rightKey = false;

    // 50% chance of shooting when asked to
    boolean shoot = r.nextDouble() <= 0.5;
    if (shoot) {
      this.click = action[Bot.KEY_CLICK];
    } else this.click = false;
  }

  /**
   * Finds the closest player
   * @return The player who is the closest to the bot
   */
  public Player findTarget() {
    Player target = null;
    double targetDistance = Double.POSITIVE_INFINITY;
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());


    System.out.println("PLAYERS LENGTH: " + allPlayers.size());
    for (Player p : allPlayers) {
      Vector2 playerPos = new Vector2((float) p.getX(), (float) p.getY());
      double distance = botPos.exactMagnitude(playerPos);
      // Update the target if another player is closer
      if (distance < targetDistance && p.isActive()) {
        targetDistance = distance;
        target = p;
      }
    }
    // Returns null if no active player is found.
    return target;
  }

  public LevelHandler getLevelHandler() {
    return levelHandler;
  }
}

