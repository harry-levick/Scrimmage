package shared.gameObject.UI;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public class Health extends GameObject {

  public Health(double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void initialiseAnimation() {
  }

  @Override
  public String getState() {
    // TODO Auto-generated method stub
    return null;
  }


}
