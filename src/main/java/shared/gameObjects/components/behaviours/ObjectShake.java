package shared.gameObjects.components.behaviours;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import javafx.scene.image.ImageView;
import shared.gameObjects.components.Behaviour;

public class ObjectShake extends Behaviour implements Serializable {
  
  private double objectX;
  private double objectY;

  private int alt = 1;

  public ObjectShake(GameObject parent) {
    super(parent);
    // TODO Auto-generated constructor stub
    objectX = parent.getX();
    objectY = parent.getY();
    
  }
  
  private void updateObjectPosition() {
    objectX = getParent().getX();
    objectY = getParent().getY();
  }
  
  private void testMovement() {
    if(alt==-1) {
      System.out.println("moving");
      getParent().getImageView().setTranslateX(objectX+100);
      getParent().getImageView().setTranslateY(objectY+100);
    }
    else {
      System.out.println("resetting.");
      getParent().getImageView().setTranslateX(objectX-50);
      getParent().getImageView().setTranslateY(objectY-50);
    }
    alt = alt * -1;
  }
  
  @Override
  public void update() {
    updateObjectPosition();
    testMovement();
  }

}
