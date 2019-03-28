package shared.gameObjects.score;

import client.handlers.effectsHandler.emitters.LineEmitter;
import client.main.Settings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.data.Collision;
import shared.physics.types.ColliderLayer;
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
    super.initialise(root, settings);
     podium2 = (Podium2) settings.getLevelHandler().getGameObjects().get(UUID.fromString("180e405a-e3ac-46f4-9480-67f31496ea72"));
     podium3 = (Podium3) settings.getLevelHandler().getGameObjects().get(UUID.fromString("5883e6ae-558a-4db1-82c3-2dcf353ec2a5"));
     podium4 = (Podium4) settings.getLevelHandler().getGameObjects().get(UUID.fromString("15699b59-f31b-4708-9ae0-97ef77333c9d"));
     System.out.println("HI");
     ArrayList<Player> players = new ArrayList();
     settings.getLevelHandler().getPlayers().forEach((uuid, player) -> players.add(player));
     Comparator<Player> compareScore = Comparator.comparing(Player::getScore);
     Collections.sort(players,compareScore);
    if (players.get(0) != null) {
      players.get(0).getTransform().setPos(this.getTransform().getPos().add(new Vector2(50, -300)));
    }
     //if (players.get(1) != null) players.get(1).getTransform().setPos(podium2.getTransform().getPos().add(new Vector2(50,-300)));
     // if (players.get(2) != null) players.get(2).getTransform().setPos(podium3.getTransform().getPos().add(new Vector2(50,-300)));
     //if (players.get(3) != null) players.get(3).getTransform().setPos(podium4.getTransform().getPos().add(new Vector2(50,-300)));
  }

  public void OnCollisionEnter(Collision c) {
    if (c.getCollidedObject() instanceof Player && !triggered) {
      settings.getLevelHandler().addGameObject(
          new LineEmitter(transform.getPos().add(new Vector2(20, 0)), new Vector2(-300, -400),
              Vector2.Zero(),
              new Vector2(8, 8), 70, 0.55f, 10f, 2, "images/particle/bloodParticle.png"));
      triggered = true;
    }
  }

}

