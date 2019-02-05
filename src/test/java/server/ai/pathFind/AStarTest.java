package server.ai.pathFind;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.Test;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;

public class AStarTest {

  @Test
  public void testPathFind() {
    ArrayList<GameObject> allObjs = new ArrayList<GameObject>();


    Player testPlayer = new Player(20, 20, 50, 20, UUID.randomUUID());
    Bot testBot = new Bot(100, 20, 50, 20, UUID.randomUUID());

    allObjs.add(testBot);
    allObjs.add(testPlayer);

    AStar AStarTest = new AStar(allObjs, testBot);

    boolean[] action = AStarTest.optimise(testPlayer);

    System.out.println(action.toString());
  }

}
