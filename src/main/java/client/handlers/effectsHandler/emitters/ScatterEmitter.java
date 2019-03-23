package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import shared.util.maths.Vector2;

public class ScatterEmitter extends ParticleEmitter {

  float scatterFactor;

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
