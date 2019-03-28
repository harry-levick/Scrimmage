package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import java.util.Random;
import shared.util.maths.Vector2;

/**
 * Class to handle emission of Circle-based particles
 */
public class CircleEmitter extends ParticleEmitter {

  private int direction;

  /**
   * Constructor:
   *
   * @param sourcePosition Centre position of the emitter
   * @param initialVelocity Maximum initial velocity particles will be spawned it
   * @param acceleration Constant acceleration of particles in their lifetime
   * @param size Max size of particles
   * @param spawnRadius Distance to spawn particles from the source position
   * @param lifetime Lifetime of the particles emitted
   * @param particleEmitterLifetime Lifetime of the emitter
   * @param particleAmount Number of particles spawned on each update frame
   * @param inwards Whether the particles begin moving towards the centre or away from the centre
   * @param imageSource The filepath of the image to use for the particle
   */
  public CircleEmitter(
      Vector2 sourcePosition,
      Vector2 initialVelocity,
      Vector2 acceleration,
      Vector2 size,
      float spawnRadius,
      float lifetime,
      float particleEmitterLifetime,
      int particleAmount,
      boolean inwards,
      String imageSource) {
    super(
        sourcePosition,
        initialVelocity,
        acceleration,
        size,
        spawnRadius,
        lifetime,
        particleEmitterLifetime,
        particleAmount,
        inwards ? ParticleType.INWARDS : ParticleType.OUTWARDS,
        imageSource);
    direction = inwards ? -1 : 1;
  }

  @Override
  protected Particle newParticle() {
    Random random = new Random();
    double angle = random.nextDouble() * 2 * Math.PI;
    double tempRadius = random.nextDouble() * radius;
    Vector2 newPos = new Vector2(radius * Math.cos(angle), radius * Math.sin(angle));

    return new Particle(transform.getPos().add(newPos),
        velocity.mult(newPos.normalize()).mult(direction), acceleration, particleSize, imageSource,
        lifetime);
  }
}
