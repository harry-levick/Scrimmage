package shared.gameObjects.background;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.rendering.ColorFilters;

public abstract class Background extends GameObject {

  Parallax parallax;

  
  public Background(UUID objectUUID) {
    super(0, 0, 1920, 1080, ObjectType.Background, objectUUID);
    initialiseAnimation();

    
  }
  

  
 

}
  
 