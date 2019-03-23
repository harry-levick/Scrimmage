package client.handlers.effectsHandler.emitters;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ParticleType;
import java.util.Random;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.physics.Physics;
import shared.util.maths.Vector2;

/** @author fxa579 Base class of ParticleEmitter Effects */
public abstract class ParticleEmitter extends GameObject {

  private Vector2 velocity;
  private Vector2 acceleration;
  private Vector2 particleSize;
  private float radius;
  private float particleEmitterLifetime;
  private float lifetime;
  private ParticleType type;
  private int particleAmount;
  private String imageSource;

  protected Vector2 previousPosition;
  protected Random random;

  public ParticleEmitter(
      Vector2 sourcePosition,
      Vector2 initialVelocity,
      Vector2 acceleration,
      Vector2 size,
      float spawnRadius,
      float lifetime,
      float particleEmitterLifetime,
      int particleAmount,
      ParticleType type,
      String imageSource) {
    super(
        sourcePosition.getX(),
        sourcePosition.getY(),
        size.getX(),
        size.getY(),
        ObjectType.Bot,
        UUID.randomUUID());
    this.velocity = initialVelocity;
    this.acceleration = acceleration;
    this.particleSize = size;
    this.radius = spawnRadius;
    this.lifetime = lifetime;
    this.particleAmount = particleAmount;
    this.particleEmitterLifetime = particleEmitterLifetime;
    this.type = type;
    this.imageSource = imageSource;
    previousPosition = Vector2.Zero();
    random = new Random();
  }

  @Override
  public void update() {
    super.update();
    if (particleEmitterLifetime <= 0) {
      settings.getLevelHandler().removeGameObject(this);
    } else {
      for(int i = 0; i < particleAmount; i++) {
        settings.getLevelHandler().addGameObject(newParticle());
      }
      particleEmitterLifetime -= Physics.TIMESTEP;
    }
  }

  /**
   * Generates a new particle, calculating its position and velocity based off of the emitter
   * @return
   */
  protected abstract Particle newParticle();

  @Override
  public void initialiseAnimation() {
    //Has no image
    this.animation.supplyAnimation("default", "images/empty.png");
  }
}
