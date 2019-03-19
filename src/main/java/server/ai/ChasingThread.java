package server.ai;

import java.util.List;
import server.ai.pathFind.AStar;
import shared.gameObjects.players.Player;

public class ChasingThread extends Thread {

  /** The bot that the thread is path-finding for */
  Bot bot;
  /** The Astar finder used in finding the path */
  AStar pathFinder;
  /** The bots target player */
  Player targetPlayer;
  /** The plan that the thread shares with the respective bot */
  public List<boolean[]> plan;
  /** Flag that governs the main loop of the thread */
  boolean running;
  /** The time delay taken at the end of each loop in the thread */
  int TIME_TO_SLEEP = 300;

  /**
   * Create a new thread
   * @param bot The respective bot
   * @param plan The plan that the thread shares with the bot
   */
  public ChasingThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler());
    running = true;
  }

  @Override
  public void run() {
    while (running) {
      targetPlayer = bot.findTarget();

      List<boolean[]> tempList = pathFinder.optimise(targetPlayer, FSA.CHASING);
      plan.clear();
      plan.addAll(tempList);

      try {
        Thread.sleep(TIME_TO_SLEEP);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
  }

  /**
   * Terminate the while loop of the thread.
   */
  public void terminate() {
    running = false;
  }


}
