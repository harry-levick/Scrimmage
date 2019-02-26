package shared.gameObjects.objects;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;

public class Spikes extends GameObject {

  public Spikes(double x, double y, double sizeX, double sizeY, ObjectID id, UUID exampleUUID) {
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
    this.animation.supplyAnimation("default", "images/objects/debrisStone_1.png");
  }

  @Override
  public void OnCollisionEnter(Collision col) {
    if (col.getCollidedObject() instanceof Player) {
      Player player = (Player) col.getCollidedObject();
      player.deductHp(10);
      ((Rigidbody) player.getComponent(ComponentType.RIGIDBODY)).moveY(-90, 0.08f);
    }
  }

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
  }
}
