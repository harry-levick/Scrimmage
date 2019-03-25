package shared.gameObjects.score;

import client.handlers.effectsHandler.emitters.LineEmitter;
import java.util.UUID;
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

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   */
  public Podium1(
      double x, double y, double sizeX, double sizeY, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, ObjectType.Podium, exampleUUID);
    addComponent(new BoxCollider(this, ColliderLayer.PLATFORM, false));
    addComponent(new Rigidbody(0, this));
  }

  // Initialise the animation
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/scoreScreen/Podium1.png");
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

