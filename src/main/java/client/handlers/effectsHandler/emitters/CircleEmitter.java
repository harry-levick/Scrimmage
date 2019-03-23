package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import shared.util.maths.Vector2;

/**
 * Class to handle emission of Circle-based particles
 */
public class CircleEmitter extends ParticleEmitter{
  private int direction;

  /**
   * Constructor:
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
        ParticleType.SCATTER,
        imageSource);
    direction = inwards ? -1 : 1;
  }

  @Override
  protected Particle newParticle() {
    return null;
  }
}
