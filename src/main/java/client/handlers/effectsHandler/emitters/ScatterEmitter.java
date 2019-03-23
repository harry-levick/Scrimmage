package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import shared.util.maths.Vector2;

/**
 * Class to handle emission of Scatter-based particles
 */
public class ScatterEmitter extends ParticleEmitter {

  float scatterFactor;

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
   * @param scatterFactor The average factor to scatter the Particle spawn values (normalized to [0-1])
   * @param imageSource The filepath of the image to use for the particle
   */

  public ScatterEmitter(
      Vector2 sourcePosition,
      Vector2 initialVelocity,
      Vector2 acceleration,
      Vector2 size,
      float spawnRadius,
      float lifetime,
      float particleEmitterLifetime,
      int particleAmount,
      float scatterFactor,
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
    this.scatterFactor = scatterFactor;
  }

  @Override
  protected Particle newParticle() {
    return null;
  }
}
