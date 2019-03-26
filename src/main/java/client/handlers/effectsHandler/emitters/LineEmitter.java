package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import java.util.Random;
import shared.util.maths.Vector2;

/**
 * Class to handle emission of Line-based particles.
 */
public class LineEmitter extends ParticleEmitter {
  private Vector2 leftPos;

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
        ParticleType.LINE,
        imageSource);
    this.leftPos = sourcePosition.sub(new Vector2(radius/2, 0));
  }

  @Override
  protected Particle newParticle() {
    Random random = new Random();
    Vector2 newPos = leftPos.add(new Vector2(random.nextDouble()*radius, 0));
    return new Particle(newPos, velocity.mult(random.nextFloat()), acceleration, particleSize, imageSource, 0.2f);
  }
}
