package server.ai;

import java.util.List;
import server.ai.pathFind.AStar;
import shared.gameObjects.players.Player;

public class BotThread extends Thread {

  Bot bot;
  AStar pathFinder;
  Player targetPlayer;
  public List<boolean[]> plan;
  boolean running;

  public BotThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler());
    running = true;
  }

  public void run() {
    while (running) {
      System.out.println("THREAD RUNNING");
      targetPlayer = bot.findTarget();

      List<boolean[]> tempList = pathFinder.optimise(targetPlayer);
      plan.clear();
      plan.addAll(tempList);

    }
    System.out.println("THREAD STOPPED");
  }

  public void terminate() {
    running = false;
  }

}
