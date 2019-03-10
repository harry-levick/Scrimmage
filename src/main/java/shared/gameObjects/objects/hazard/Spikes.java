package shared.gameObjects.objects.hazard;

import java.util.UUID;
import shared.gameObjects.Destructable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;

public class Spikes extends GameObject {

  public Spikes(double x, double y, double sizeX, double sizeY, ObjectType id, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, id, exampleUUID);
    addComponent(new BoxCollider(this, ColliderLayer.OBJECT, false));
    addComponent(
        new Rigidbody(
            RigidbodyType.STATIC,
            0,
            0,
            0,
            new MaterialProperty(0.2f, 0.2f, 0.1f),
            new AngularData(0, 0, 0, 0),
            this));
  }

  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/objects/debris/debrisStone_1.png");
  }

  @Override
  public void OnCollisionEnter(Collision col) {
    if (col.getCollidedObject() instanceof Destructable) {
      Destructable player = (Destructable) col.getCollidedObject();
      player.deductHp(10);
    }
  }

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
  }
}
