package shared.gameObject.UI;

import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

import java.util.UUID;

public class Timer extends GameObject {

  public Timer(double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void initialiseAnimation() {}

  @Override
  public String getState() {
    // TODO Auto-generated method stub
    return null;
  }
}
