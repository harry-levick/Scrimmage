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

  protected Vector2 velocity;
  protected Vector2 acceleration;
  protected Vector2 particleSize;
  protected float radius;
  private float particleEmitterLifetime;
  protected float lifetime;
  private ParticleType type;
  private int particleAmount;
  protected String imageSource;

  /**
   * Position the last particle was spawned at
   */
  protected Vector2 previousPosition;
  /**
   * RNG for particle spawning
   */
  protected Random random;

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
   * @param type The type of particle emitter (Line, Scatter, etc.)
   * @param imageSource The filepath of the image to use for the particle
   */
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
   * @return Particle Game Object to instantiate
   */
  protected abstract Particle newParticle();

  @Override
  public void initialiseAnimation() {
    //Has no image
    this.animation.supplyAnimation("default", "images/empty.png");
  }
}
