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
  boolean running;

  public BotThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler().getGameObjects());
    running = true;
  }

  public void run() {
    while (running) {
      targetPlayer = bot.findTarget();


      List<boolean[]> tempList = pathFinder.optimise(targetPlayer);
      plan.clear();
      plan.addAll(tempList);



      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("BOT RUNNING -----------------------------------");

    }
    System.out.println("BOT TERMINATED -----------------------------------");
  }

  public void terminate() {
    running = false;
    System.out.println("BOT TERMINATED -----------------------------------");
  }

}
