package client.handlers.effectsHandler;

import client.main.Client;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.physics.Physics;
import shared.util.maths.Vector2;

/**
 * @author fxa579 Base class of Particle Effects
 */
public class Particle {

  private Vector2 velocity;
  private Vector2 acceleration;
  private Vector2 particleSize;
  private float radius;
  private float lifetime;

  public Particle(Vector2 sourcePosition, Vector2 initialVelocity, Vector2 acceleration, Vector2 size, float spawnRadius, float lifetime) {

  }

}
