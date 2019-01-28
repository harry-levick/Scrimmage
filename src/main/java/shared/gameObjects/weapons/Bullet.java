package shared.gameObjects.weapons;

import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

/**
 * @author hlf764
 */
public class Bullet extends GameObject {
  
  private double width;     // width of bullet
  private double speed;     // speed of bullet
  
  public Bullet(
      double x,
      double y,
      ObjectID id,
      double width,
      double speed
      ) {
    
    super(x, y, id, "path/to/bulletImage");
    this.width = width;
    this.speed = speed;
  }
  
  public void update() {
    
  }
  
  public void render() {
    
  }
  
}
