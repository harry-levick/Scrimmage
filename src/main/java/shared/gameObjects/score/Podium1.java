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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketAward;
import shared.packets.PacketAward.AwardID;
import shared.physics.data.Collision;
import shared.physics.types.ColliderLayer;
import shared.util.Path;
import shared.util.maths.Vector2;

public class Podium1 extends GameObject {

  boolean triggered = false;
  private Podium2 podium2;
  private Podium3 podium3;
  private Podium4 podium4;

  private transient Text score0;
  private transient Text score1;
  private transient Text score2;
  private transient Text score3;

  private static final int FIRST_MONEY = 150;
  private static final int SECOND_MONEY = 100;
  private static final int THIRD_MONEY = 50;
  private static final int FOURTH_MONEY = 0;

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
    //Kill Bots
    settings.getLevelHandler().getGameObjects().keySet()
        .removeAll(settings.getLevelHandler().getBotPlayerList().keySet());
    settings.getLevelHandler().getBotPlayerList().forEach((key, bot) -> bot.terminate());
    super.initialise(root, settings);
    //Create Podiums
    podium2 = new Podium2(getX() + 320, getY() + 160, 240, 320);
    settings.getLevelHandler().addGameObject(podium2);
    podium3 = new Podium3(getX() - 320, getY() + 240, 240, 240);
    settings.getLevelHandler().addGameObject(podium3);
    podium4 = new Podium4(getX() + 640, getY() + 360, 240, 120);
    settings.getLevelHandler().addGameObject(podium4);
     ArrayList<Player> players = new ArrayList();
    //Create player replacements for each bot
    settings.getLevelHandler().getPlayers().forEach((uuid, player) -> {
      if (player instanceof Bot) {
        Player playerCopy = new Player(player.getX(), player.getY(), player.getUUID());
        System.out.println("Using skin: " + player.getCurrentSkin());
        playerCopy.setScore(player.score);
        playerCopy.setUsername(player.getUsername());
        settings.getLevelHandler().addGameObject(playerCopy);
        playerCopy.updateSkinRender(player.getCurrentSkin());
        players.add(playerCopy);
      } else {
        players.add(player);
      }
    });
    //Double destory bots
    settings.getLevelHandler().getPlayers().keySet()
        .removeAll(settings.getLevelHandler().getBotPlayerList().keySet());
    settings.getLevelHandler().getBotPlayerList()
        .forEach((key, gameObject) -> gameObject.removeRender());
    settings.getLevelHandler().getBotPlayerList().forEach((key, gameObject) -> gameObject = null);
    settings.getLevelHandler().getBotPlayerList().clear();

     Comparator<Player> compareScore = Comparator.comparing(Player::getScore);
    Collections.sort(players, compareScore.reversed());
    //1st
    if (players.size() > 0) {
      Vector2 pos0 = this.getTransform().getPos().add(new Vector2(80, -300));
      players.get(0).getTransform().setPos(pos0);
      score0 = new Text(String.valueOf(players.get(0).getScore()));
      score0.setTranslateX(pos0.getX());
      score0.setTranslateY(pos0.getY());
      score0.setFill(Color.WHITE);
      score0.setFont(settings.getFont(64));
      root.getChildren().add(score0);
      if(settings.getLevelHandler().isServer()) {
        settings.getLevelHandler().getServer().sendToClients(new PacketAward(AwardID.MONEY, FIRST_MONEY, players.get(0)).getData(), false);
      } else {
        if(players.get(0).equals(settings.getLevelHandler().getClientPlayer())) settings.getData().addMoney(FIRST_MONEY);
      }
    }
    //2nd
    if (players.size() > 1) {
      Vector2 pos1 = podium2.getTransform().getPos().add(new Vector2(80, -300));
      players.get(1).getTransform()
          .setPos(pos1);
      score1 = new Text(String.valueOf(players.get(1).getScore()));
      score1.setTranslateX(pos1.getX());
      score1.setTranslateY(pos1.getY());
      score1.setFill(Color.WHITE);
      score1.setFont(settings.getFont(64));
      root.getChildren().add(score1);
      if(settings.getLevelHandler().isServer()) {
        settings.getLevelHandler().getServer().sendToClients(new PacketAward(AwardID.MONEY, SECOND_MONEY, players.get(1)).getData(), false);
      } else {
        if(players.get(1).equals(settings.getLevelHandler().getClientPlayer())) settings.getData().addMoney(SECOND_MONEY);
      }
     }
    //3rd
    if (players.size() > 2) {
      Vector2 pos2 = podium3.getTransform().getPos().add(new Vector2(80, -300));
     players.get(2).getTransform()
         .setPos(pos2);
      score2 = new Text(String.valueOf(players.get(2).getScore()));
      score2.setTranslateX(pos2.getX());
      score2.setTranslateY(pos2.getY());
      score2.setFill(Color.WHITE);
      score2.setFont(settings.getFont(64));
      root.getChildren().add(score2);
      if(settings.getLevelHandler().isServer()) {
        settings.getLevelHandler().getServer().sendToClients(new PacketAward(AwardID.MONEY, THIRD_MONEY, players.get(2)).getData(), false);
      } else {
        if(players.get(2).equals(settings.getLevelHandler().getClientPlayer())) settings.getData().addMoney(THIRD_MONEY);
      }
     }
    //4th
    if (players.size() > 3) {
      Vector2 pos3 = podium4.getTransform().getPos().add(new Vector2(80, -300));
      players.get(3).getTransform()
          .setPos(pos3);
      score3 = new Text(String.valueOf(players.get(3).getScore()));
      score3.setTranslateX(pos3.getX());
      score3.setTranslateY(pos3.getY());
      score3.setFill(Color.WHITE);
      score3.setFont(settings.getFont(64));
      root.getChildren().add(score3);
      if(settings.getLevelHandler().isServer()) {
        settings.getLevelHandler().getServer().sendToClients(new PacketAward(AwardID.MONEY, FOURTH_MONEY, players.get(3)).getData(), false);
      } else {
        if(players.get(3).equals(settings.getLevelHandler().getClientPlayer())) settings.getData().addMoney(FOURTH_MONEY);
      }
     }
    //Go back
    new java.util.Timer().schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                if (settings.getLevelHandler().getClientPlayer() != null) {
                  players.remove(settings.getLevelHandler().getClientPlayer());
                }
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

  @Override
  public void removeRender() {
    super.removeRender();
    Platform.runLater(
        () -> {
          root.getChildren().remove(score0);
          root.getChildren().remove(score1);
          root.getChildren().remove(score2);
          root.getChildren().remove(score3);
        }
    );
  }

  public void OnCollisionEnter(Collision c) {
    if (c.getCollidedObject() instanceof Player && !triggered) {
      settings.getLevelHandler().addGameObject(
          new ServerParticle(transform.getPos().add(new Vector2(20, 0)), new Vector2(100, -540),
              Vector2.Zero(),
              new Vector2(256, 256), "fireworks", 1.2f));
      settings.getLevelHandler().addGameObject(
          new ServerParticle(transform.getPos().add(new Vector2(20, 0)), new Vector2(-100, -540),
              Vector2.Zero(),
              new Vector2(256, 256), "fireworks", 1.2f));
      for(int i = 0; i < 8; i++) {
      }
      triggered = true;
    }
  }

}

