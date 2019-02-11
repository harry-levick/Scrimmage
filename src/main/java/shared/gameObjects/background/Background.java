package shared.gameObjects.background;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;


public class Background extends GameObject{

  private String imagePath;
  
  public Background(String imgPath, ObjectID id, UUID objectUUID) {
    super(0,0,1920,1080, id, objectUUID);
    imagePath = imgPath;
    initialiseAnimation();
  }

  @Override
  public void initialiseAnimation() {
    //System.out.println("imagepath:"+imagePath);
    this.animation.supplyAnimation("default", imagePath); 
  }

  @Override
  public String getState() {
    // TODO Auto-generated method stub
    return null;
  }

public void render() {
  super.render();
}
}
