package client.handlers.effectsHandler;

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

  public Particle(Vector2 sourcePosition, Vector2 initialVelocity, Vector2 acceleration,
      Vector2 size, float spawnRadius, float lifetime) {

  }

}
