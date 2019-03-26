package server.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import server.ai.pathFind.AStar.SearchNode;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Weapon;
import shared.handlers.levelHandler.LevelHandler;
import shared.util.maths.Vector2;

/**
 * @author Harry Levick (hxl799)
 */
public class Bot extends Player {

  /** The action array index for jump key */
  public static final int KEY_JUMP = 0;
  /** The action array index for left key */
  public static final int KEY_LEFT = 1;
  /** The action array index for right key */
  public static final int KEY_RIGHT = 2;
  /** The state that the bot is in */
  FSA state;
  /** The bots target */
  public Player targetPlayer;
  /** All of the players in the world, players + bots */
  LinkedHashMap<UUID, Player> allPlayers;
  /** The levelHandler passed in the constructor, used for collecting game objects */
  LevelHandler levelHandler;
  /** Thread to govern the chasing path to the enemy */
  ChasingThread chasingThread;
  /** Thread to govern the fleeing path from the enemy */
  FleeingThread fleeingThread;
  /** The list of actions to take from when the bot is chasing the enemy */
  List<boolean[]> chasingPlan;
  /** The list of actions to take from when the bot is fleeing the enemy */
  List<boolean[]> fleeingPlan;

  int prevHealth;

  /**
   * @param x x pos of the bot
   * @param y y pos of the bot
   * @param playerUUID
   * @param levelHandler
   */
  public Bot(double x, double y, UUID playerUUID, LevelHandler levelHandler) {
    super(x, y, playerUUID);
    this.prevHealth = this.health;
    this.state = FSA.INITIAL_STATE;
    this.levelHandler = levelHandler;
    this.targetPlayer = findTarget();
    this.chasingPlan = Collections.synchronizedList(new ArrayList<>());
    this.fleeingPlan = Collections.synchronizedList(new ArrayList<>());

    this.chasingThread = new ChasingThread(this, chasingPlan);
    chasingThread.setName("Bot chasingThread");
    this.fleeingThread = new FleeingThread(this, fleeingPlan);

  }

  /**
   * Constructor to copy an object of Bot.
   * @param that object to be copied
   */
  public Bot(Bot that) {
    super(that.getX(), that.getY(), UUID.randomUUID());
    this.levelHandler = that.levelHandler;

  }

  /**
   * Begins the loops of both the chasing path-finder thread and the fleeing path-finder thread.
   */
  public void startThread() {
    chasingThread.start();
    fleeingThread.start();
  }

  /**
   * Terminates the bot's threads and creates new ones, used when the map is changed.
   */
  public void reset() {
    chasingThread.terminate();
    fleeingThread.terminate();

    chasingThread = new ChasingThread(this, chasingPlan);
    fleeingThread = new FleeingThread(this, fleeingPlan);
    startThread();

    super.reset();
  }

  /**
   * Check to see if the bot can jump in its current position
   * @return True if the bot is on the ground (can jump).
   */
  public boolean mayJump() {
    return grounded;
  }

  @Override
  public void update() {

    if (!active) {
      chasingThread.terminate();
      fleeingThread.terminate();
    }
    setFalse();

    double distanceToTarget = 0;
    targetPlayer = findTarget();

    if (targetPlayer != null)
      distanceToTarget = calcDist();

    Weapon nearbyWeapon = findClosestItem(levelHandler.getGameObjects().values().stream()
        .filter(w -> w instanceof Weapon && (((Weapon) w).getHolder() == null))
        .map(Weapon.class::cast)
        .collect(Collectors.toList()));

    if (nearbyWeapon != null) {
      double distanceToWeap = (nearbyWeapon.getTransform().getPos()).magnitude(this.getTransform().getPos());
      if (distanceToWeap < 80) {
        this.throwHoldingKey = true;
      }
    }

    try {
      state = state.next(targetPlayer, this, distanceToTarget, prevHealth);
    } catch (NullPointerException e) {
      System.out.println("Null Pointer");
    }


    switch (state) {
      case IDLE:
        System.out.println("IDLE");

        break;
      case CHASING:
        System.out.println("CHASING");
        executeAction(FSA.CHASING);

        break;
      case FLEEING:
        System.out.println("FLEEING");
        executeAction(FSA.FLEEING);

        break;
      case ATTACKING:
        System.out.println("ATTACKING");
        Vector2 enemyPosCenter = targetPlayer.getTransform().getPos()
            .add(targetPlayer.getTransform().getSize().mult(0.5f));
        mouseX = enemyPosCenter.getX();
        mouseY = enemyPosCenter.getY();
        click = true;

        break;
    }
    prevHealth = this.health;
    super.update();
  }

  /**
   * Calculates the distance from the bot to the current target player
   * @return The distance to the target player
   */
  private double calcDist() {
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(),
        (float) targetPlayer.getY());
    // Calculate the distance to the target
    return botPos.exactMagnitude(targetPos);
  }

  /**
   * Executes the action given the state the bot is in (Fleeing or Attacking)
   * @param state The state the bot is in when this method is called.
   */
  private void executeAction(FSA state) {
    boolean[] action = new boolean[]{false, false, false};

    if (state == FSA.CHASING) {
      if (chasingPlan.size() > 0) {
        action = chasingPlan.remove(0);
      }

      this.jumpKey = action[Bot.KEY_JUMP];
      this.leftKey = action[Bot.KEY_LEFT];
      this.rightKey = action[Bot.KEY_RIGHT];

    } else if (state == FSA.FLEEING) {
      if (fleeingPlan.size() > 0) {
        action = fleeingPlan.remove(0);
      }
      this.jumpKey = action[Bot.KEY_JUMP];
      this.leftKey = action[Bot.KEY_LEFT];
      this.rightKey = action[Bot.KEY_RIGHT];
    }

  }

  /**
   * Updates the bot, used only by the path-finder when simulating in the world.
   */
  public void simulateUpdate() {
    super.update();
  }

  /**
   * Used by the path-finder to simulate the action of the bot, setting the movement booleans based
   * on the action given.
   * @param action The action the bot is to take.
   */
  public void simulateAction(boolean[] action) {
    this.jumpKey = action[Bot.KEY_JUMP];
    this.leftKey = action[Bot.KEY_LEFT];
    this.rightKey = action[Bot.KEY_RIGHT];
  }

  /**
   * Used by the path-finder to simulate the bots position, doesnt have the same visual
   * effects as the applyInput() method.
   */
  public void simulateApplyInput() {
    if (rightKey) {
      rb.moveX(speed);
    }
    if (leftKey) {
      rb.moveX(speed * -1);
    }
    if (!rightKey && !leftKey) {
      vx = 0;
    }
    if (jumpKey && !jumped) {
      rb.moveY(jumpForce, 0.33333f);
      jumped = true;
    }
    if (grounded) {
      jumped = false;
    }
    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    }

    if (this.getHolding() != null) {
      this.getHolding().setX(this.getX() + 60);
      this.getHolding().setY(this.getY() + 70);
    }
  }

  /**
   * Finds the closest player to the bot.
   * @return The target player
   */
  public Player findTarget() {
    allPlayers = levelHandler.getPlayers();
    allPlayers.putAll(levelHandler.getBotPlayerList());

    Player target = null;
    double targetDistance = Double.POSITIVE_INFINITY;
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());


    for (Map.Entry<UUID, Player> entry : allPlayers.entrySet()) {
      Player player = entry.getValue();

      if (player.equals(this))
        continue;

      Vector2 playerPos = new Vector2((float) player.getX(), (float) player.getY());
      double distance = botPos.exactMagnitude(playerPos);
      // Update the target if another player is closer
      if (distance < targetDistance && player.isActive()) {
        targetDistance = distance;
        target = player;
      }
    }

    // Returns null if no active player is found.
    return target;
  }

  /**
   * Finds the closest pick-upable item
   * @param allItems A list of all the items in the world.
   * @return The item that is the closest to the bot
   */
  public Weapon findClosestItem(List<Weapon> allItems) {
    Weapon closestWeap = null;
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());
    double targetDistance = Double.POSITIVE_INFINITY;

    for (Weapon weap : allItems) {
      Vector2 itemPos = new Vector2((float) weap.getX(), (float) weap.getY());
      double distance = botPos.exactMagnitude(itemPos);

      if (distance < targetDistance &&
          (weap.getWeaponRank() > this.getHolding().getWeaponRank())) {
        targetDistance = distance;
        closestWeap = weap;
      }
    }

    return closestWeap;
  }

  /**
   * Set all of the movement booleans false.
   */
  private void setFalse() {
    jumpKey = false;
    leftKey = false;
    rightKey = false;
    click = false;
    throwHoldingKey = false;
  }

  /**
   * Returns the LevelHandler that was given to the bot on creation.
   * @return LevelHandler
   */
  public LevelHandler getLevelHandler() {
    return levelHandler;
  }

  /**
   * Get the bots target player
   */
  public Player getTargetPlayer() { return targetPlayer; }

}
