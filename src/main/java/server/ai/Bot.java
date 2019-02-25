package server.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/*
  TODO Week 7:
    - Make path finder use level handler
    - Path finder finds path to a stationary enemy
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
    this.targetPlayer = findTarget();
    this.plan = Collections.synchronizedList(new ArrayList<>());

    this.botThread = new BotThread(this, plan);
  }

  /**
   * Copy constructor
   * @param that object to be copied
   */
  public Bot(Bot that) {
    this(that.getX(), that.getY(), that.getUUID(), that.getLevelHandler());
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

    /*

    if (!active) {
      botThread.terminate();
    }
     */

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
        executeAction();

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
    //System.out.println("x = " + targetPlayer.getX() + ", y = " + targetPlayer.getY());
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(),
        (float) targetPlayer.getY());
    // Calculate the distance to the target
    return botPos.exactMagnitude(targetPos);
  }

  private void executeAction() {
    boolean[] action = new boolean[] {false, false, false, false};

    //System.out.println("PLAN SIZE: " + plan.size());
    if (plan.size() > 0) {
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

  }

  public void simulateUpdate() {
    super.update();
  }

  public void simulateAction(boolean[] action) {
    this.jumpKey = action[Bot.KEY_JUMP];
    this.leftKey = action[Bot.KEY_LEFT];
    this.rightKey = action[Bot.KEY_RIGHT];
  }

  public void simulateApplyInput() {
    if (rightKey) {
      rb.moveX(speed);
      //animation.switchAnimation("walk");
      //imageView.setScaleX(1);
    }
    if (leftKey) {
      rb.moveX(speed * -1);
      //animation.switchAnimation("walk");
      //imageView.setScaleX(-1);
    }

    if (!rightKey && !leftKey) {
      vx = 0;
      //animation.switchDefault();

    }
    if (jumpKey && !jumped) {
      rb.moveY(jumpForce, 0.33333f);
      jumped = true;
    }
    if (jumped) {
      //animation.switchAnimation("jump");
    }
    if (grounded) {
      jumped = false;
    }
    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    } //else punch
    //setX(getX() + (vx * 0.0166));

    if (this.getHolding() != null) {
      this.getHolding().setX(this.getX() + 60);
      this.getHolding().setY(this.getY() + 70);
    }
  }

  /**
   * Finds the closest player
   * @return The player who is the closest to the bot
   */
  public Player findTarget() {
    allPlayers = levelHandler.getPlayers();
    /*

    // Collect all players (other than bots) from the world
    allPlayers = allPlayers.stream()
        .filter(p -> p instanceof Player)
        .map(Player.class::cast)
        .collect(Collectors.toList());
     */
    // Remove bots from player list
    allPlayers.removeAll(levelHandler.getBotPlayerList());

    Player target = null;
    double targetDistance = Double.POSITIVE_INFINITY;
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());


    for (Player p : allPlayers) {
      Vector2 playerPos = new Vector2((float) p.getX(), (float) p.getY());
      double distance = botPos.exactMagnitude(playerPos);
      // Update the target if another player is closer
      if (distance < targetDistance && p.isActive()) {
        targetDistance = distance;
        target = p;
      }
    }
    System.out.println("targetx = " + target.getX() + ", targety = " + target.getY());
    System.out.println("botx = " + this.getX() + ", boty = " + this.getY());
    // Returns null if no active player is found.
    return target;
  }

  public LevelHandler getLevelHandler() {
    return levelHandler;
  }
}

