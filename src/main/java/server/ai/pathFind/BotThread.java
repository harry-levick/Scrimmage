package server.ai.pathFind;

import java.util.ArrayList;
import java.util.List;
import server.ai.Bot;
import shared.gameObjects.players.Player;
import shared.util.maths.Vector2;

public class BotThread extends Thread {

  Bot bot;
  AStar pathFinder;
  Player targetPlayer;
  public List<boolean[]> plan;

  public BotThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler());
  }

  public void run() {
    while (true) {
      targetPlayer = bot.targetPlayer;
      synchronized (plan) {
        plan.clear();
        plan.addAll(pathFinder.optimise(targetPlayer));
      }
    }
  }

  /**
   * returns the first action of the plan
   * @return
   */
  public boolean[] getNextAction() {
    return plan.remove(0);
  }

}
