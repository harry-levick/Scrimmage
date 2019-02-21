package shared.gameObjects.menu.main;

import client.main.Client;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import server.ai.Bot;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.ButtonObject;
import shared.gameObjects.weapons.MachineGun;
import shared.gameObjects.weapons.Sword;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonSingleplayer extends ButtonObject {

  private final int maxPlayers = 1;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonSingleplayer(
      double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation(
        "images/buttons/singleplayer_unpressed.png", "images/buttons/singleplayer_pressed.png");
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    //System.out.println("test");
    Client.levelHandler.changeMap(new Map("map1", Path.convert("src/main/resources/menus/menu.map"),
        GameState.IN_GAME), true);
    int botsToAdd = maxPlayers - Client.levelHandler.getPlayers().size();
    for (int b = 0; b < botsToAdd; b++) {
      Bot botPlayer = new Bot(500, 500, UUID.randomUUID(), Client.levelHandler.getGameObjects());
      botPlayer.setHolding(
          new Sword(500, 500, "Sword@LevelHandler", botPlayer, UUID.randomUUID())
      );
      botPlayer.getHolding().initialise(Client.gameRoot);
      botPlayer.initialise(Client.gameRoot);
      Client.levelHandler.getPlayers().add(botPlayer);
      Client.levelHandler.getBotPlayerList().add(botPlayer);
      Client.levelHandler.getGameObjects().add(botPlayer);
      Client.levelHandler.getGameObjects().add(botPlayer.getHolding());
    }

    Client.levelHandler.getClientPlayer().setHolding(
        new MachineGun(500, 500, "MachineGun@LevelHandler", Client.levelHandler.getClientPlayer(),
            UUID.randomUUID()));
    Client.levelHandler.getGameObjects().add(Client.levelHandler.getClientPlayer().getHolding());
    Client.levelHandler.getClientPlayer().getHolding().initialise(Client.gameRoot);
    Client.singleplayerGame = true;
    Client.timer.schedule(Client.task, 300000L);
    //System.out.println("test2");
  }
}
