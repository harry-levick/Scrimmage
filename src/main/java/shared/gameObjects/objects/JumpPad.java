package shared.gameObjects.objects;

import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class JumpPad extends GameObject {

  protected boolean spring, cooldown;
  protected float timer;
  protected Vector2 originalPosition;
  final float TIME_TO_JUMP = 0.5f;
  final float SPRING_FORCE = -450f/0.33333f;
  final float SQUISH_FACTOR = 0.05f;

  public JumpPad(double x, double y, UUID uuid) {
    super(x, y, 50, 40, ObjectType.Bot, uuid);
    addComponent(new BoxCollider(this, ColliderLayer.WALL, true));
    addComponent(new BoxCollider(this, ColliderLayer.COLLECTABLE, false));
    addComponent(new Rigidbody(0, this));
    originalPosition = transform.getPos();
    timer = TIME_TO_JUMP;
  }

  @Override
  public void update() {
    super.update();
    if(spring) {
      transform.translate(Vector2.Down().mult(SQUISH_FACTOR));
      timer -= Physics.TIMESTEP;
      if(timer <= 0) {
        spring = false;
        cooldown = true;
        timer = TIME_TO_JUMP;
        ArrayList<Collision> collisions = Physics.boxcastAll(transform.getPos().add(Vector2.Up().mult(30)), transform.getSize());
        for (Collision c : collisions) {
          Rigidbody rb = (Rigidbody) c.getCollidedObject().getComponent(ComponentType.RIGIDBODY);
          if (rb.getBodyType() == RigidbodyType.DYNAMIC) {
            rb.setVelocity(new Vector2(rb.getVelocity().getX(), SPRING_FORCE));
          }
        }
      }
    } else {
      transform.setPos(originalPosition);
    }
    if(cooldown) {
      timer -= Physics.TIMESTEP;
      if(timer <= 0) {
        cooldown = false;
        timer = TIME_TO_JUMP;
      }
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/objects/jumpPad.png");
  }

  @Override
  public void OnTriggerEnter(Collision col) {
    super.OnCollisionEnter(col);
    if(!cooldown && col.getNormalCollision().equals(Vector2.Up())) {
      System.out.println("Entered");
      spring = true;
    }

  }
}
