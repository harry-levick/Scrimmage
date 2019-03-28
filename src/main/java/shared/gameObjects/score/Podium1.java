package shared.gameObjects.score;

import client.handlers.effectsHandler.ServerParticle;
import client.handlers.effectsHandler.emitters.LineEmitter;
import client.main.Settings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import javafx.application.Platform;
import javafx.scene.Group;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.Map;
import shared.physics.data.Collision;
import shared.physics.types.ColliderLayer;
import shared.util.Path;
import shared.util.maths.Vector2;

public class Podium1 extends GameObject {

  boolean triggered = false;
  private Podium2 podium2;
  private Podium3 podium3;
  private Podium4 podium4;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   */
  public Podium1(
      double x, double y, double sizeX, double sizeY) {
    super(x, y, sizeX, sizeY, ObjectType.Podium,
        UUID.fromString("49871ee2-da35-49d5-9793-5bea1b253147"));
    addComponent(new BoxCollider(this, ColliderLayer.PLATFORM, false));
    addComponent(new Rigidbody(0, this));
  }

  // Initialise the animation
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/scoreScreen/Podium1.png");
  }

  public void initialise(Group root, Settings settings) {
    settings.getLevelHandler().getGameObjects().keySet()
        .removeAll(settings.getLevelHandler().getBotPlayerList().keySet());
    settings.getLevelHandler().getBotPlayerList().forEach((key, bot) -> bot.terminate());
    super.initialise(root, settings);
    podium2 = new Podium2(getX() + 320, getY() + 120, 240, 320);
    settings.getLevelHandler().addGameObject(podium2);
    podium3 = new Podium3(getX() - 320, getY() + 40, 240, 240);
    settings.getLevelHandler().addGameObject(podium3);
    podium4 = new Podium4(getX() + 640, getY() + 320, 240, 120);
    settings.getLevelHandler().addGameObject(podium4);
     ArrayList<Player> players = new ArrayList(4);
    settings.getLevelHandler().getPlayers().forEach((uuid, player) -> {
      if (player instanceof Bot) {
        Player playerCopy = new Player(player.getX(), player.getY(), player.getUUID());
        playerCopy.setScore(player.score);
        playerCopy.setUsername(player.getUsername());
        settings.getLevelHandler().addGameObject(playerCopy);
        players.add(playerCopy);
      } else {
        players.add(player);
      }
    });
    settings.getLevelHandler().getPlayers().keySet()
        .removeAll(settings.getLevelHandler().getBotPlayerList().keySet());
    settings.getLevelHandler().getBotPlayerList()
        .forEach((key, gameObject) -> gameObject.removeRender());
    settings.getLevelHandler().getBotPlayerList().forEach((key, gameObject) -> gameObject = null);
    settings.getLevelHandler().getBotPlayerList().clear();

     Comparator<Player> compareScore = Comparator.comparing(Player::getScore);
     Collections.sort(players,compareScore);
    if (players.get(0) != null) {
      players.get(0).getTransform().setPos(this.getTransform().getPos().add(new Vector2(50, -300)));
    }
    if (players.get(1) != null) {
      players.get(1).getTransform()
          .setPos(podium2.getTransform().getPos().add(new Vector2(50, -300)));
    }
    if (players.get(2) != null) {
      players.get(2).getTransform()
          .setPos(podium3.getTransform().getPos().add(new Vector2(50, -300)));
    }
    if (players.get(3) != null) {
      players.get(3).getTransform()
          .setPos(podium4.getTransform().getPos().add(new Vector2(50, -300)));
    }
    new java.util.Timer().schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                players.forEach(player -> {
                  player.removeRender();
                });
                players.clear();
                settings.getLevelHandler().changeMap(
                    new Map("menus/main_menu.map",
                        Path.convert("src/main/resources/menus/main_menu.map")),
                    true, false);
              }
            });

          }
        }, 15000
    );
  }

  public void OnCollisionEnter(Collision c) {
    if (c.getCollidedObject() instanceof Player && !triggered) {
      settings.getLevelHandler().addGameObject(
          new ServerParticle(transform.getPos().add(new Vector2(20, 0)), new Vector2(-300, -400),
              Vector2.Zero(),
              new Vector2(128, 256), "blood", 100));
      settings.getLevelHandler().addGameObject(
          new ServerParticle(transform.getPos().add(new Vector2(20, 0)), new Vector2(300, -400),
              Vector2.Zero(),
              new Vector2(128, 256), "blood", 100));
      triggered = true;
    }
  }

}

