package client.handlers.effectsHandler;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.physics.Physics;
import shared.util.maths.Vector2;

/**
 * Visual Effect from an image
 */
public class Particle extends GameObject {

  private Vector2 velocity;
  private Vector2 acceleration;
  private String imageSource;
  private float lifetime;

  /**
   * Constructs a particle
   * @param sourcePosition Spawn position of the particle
   * @param initialVelocity Particle's initial velocity
   * @param acceleration Acceleration applied on every update
   * @param size Size of particle
   * @param imageSource Source filepath of particle image
   * @param lifetime How long the particle lives for
   */
  public Particle(
      Vector2 sourcePosition,
      Vector2 initialVelocity,
      Vector2 acceleration,
      Vector2 size,
      String imageSource,
      float lifetime) {
    super(
        sourcePosition.getX(),
        sourcePosition.getY(),
        size.getX(),
        size.getY(),
        ObjectType.Bot,
        UUID.randomUUID());
    this.velocity = initialVelocity;
    this.acceleration = acceleration;
    this.imageSource = imageSource;
    this.lifetime = lifetime;
    networkStateUpdate = true;
    initialiseAnimation();
  }

  @Override
  public void initialiseAnimation() {
    if(imageSource == null) return;
    this.animation.supplyAnimation("default", imageSource);
  }

  @Override
  public void update() {
    imageView.setOpacity(0.85);
    imageView.setRotate(velocity.angle());
    if (lifetime <= 0) {
      settings.getLevelHandler().removeGameObject(this);
    } else {
      velocity = velocity.add(acceleration.mult(Physics.TIMESTEP));
      transform.translate(velocity.mult(Physics.TIMESTEP));
      lifetime -= Physics.TIMESTEP;
    }
  }

}
