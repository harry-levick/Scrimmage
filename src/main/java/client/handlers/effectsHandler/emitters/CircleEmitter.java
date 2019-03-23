package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import shared.util.maths.Vector2;

public class CircleEmitter extends ParticleEmitter{
  private int direction;

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
