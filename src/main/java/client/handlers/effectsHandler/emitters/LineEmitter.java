package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import shared.util.maths.Vector2;

/**
 * Class to handle emission of Line-based particles
 */
public class LineEmitter extends ParticleEmitter {
  private float angle;

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
   * @param angle The angle of the line with respect to the bottom left of the grid
   * @param imageSource The filepath of the image to use for the particle
   */
  public LineEmitter(
      Vector2 sourcePosition,
      Vector2 initialVelocity,
      Vector2 acceleration,
      Vector2 size,
      float spawnRadius,
      float lifetime,
      float particleEmitterLifetime,
      int particleAmount,
      float angle,
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
    this.angle = angle;
  }

  @Override
  public void update() {

  }
  @Override
  protected Particle newParticle() {

    return new Particle(transform.getPos(), velocity, acceleration, particleSize, imageSource, 0.2f);
  }
}
