package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import shared.util.maths.Vector2;

public class LineEmitter extends ParticleEmitter {
  private float angle;

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
  protected Particle newParticle() {

    return null;
  }
}
