package shared.gameObjects.Utils;

import java.io.Serializable;
import java.sql.Timestamp;
import shared.util.maths.Vector2;
//TODO JavaDoc this boi
public class TimePosition implements Serializable {

  private Timestamp timestamp;
  private Vector2 position;

  public TimePosition(Timestamp timestamp, Vector2 position) {
    this.position = position;
    this.timestamp = timestamp;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public Vector2 getPosition() {
    return position;
  }

  public void setPosition(Vector2 position) {
    this.position = position;
  }
}
