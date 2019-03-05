package shared.gameObjects.rendering;

import javafx.scene.image.ImageView;
import shared.gameObjects.GameObject;

public class ObjectShake {
  
  private GameObject gameObject;
  private ImageView imageView;
  
  
  private double x;
  private double y;
  
  private float growth;
  private float amplitude; 
  private float frequency;
  private float amount;
  
  public boolean active = false;
  private int alt = 1;
  
  private float timer;

  public ObjectShake(GameObject gameObject,float growth,float amplitude,float frequency) { //Defaults at 5,3,100
    this.gameObject = gameObject;
    this.imageView = gameObject.getImageView();
    
    
    x = this.gameObject.getX();
    y = this.gameObject.getY();
    
    
    growth = growth;
    amplitude = amplitude;
    frequency = frequency;
    
    amount = 10.0f;
    timer = 0.0f; 
  }
  
  private void applyShake(float shakeFactor) {
    
  
    
    /*
    float newX = (float) (shakeFactor * Math.sin(timer * frequency));
    this.gameObject.getImageView().setTranslateX(x + newX);
    
    float newY = (float) (shakeFactor * Math.cos(timer * frequency));
    this.gameObject.getImageView().setTranslateY(y + newY);
    */
    
    if(alt==-1) {
      this.gameObject.getImageView().setTranslateX(x+50);
      this.gameObject.getImageView().setTranslateY(y+50);
    }
    else {
      this.gameObject.getImageView().setTranslateX(x);
      this.gameObject.getImageView().setTranslateY(y);
    }
    alt = alt * -1;
  }
   
  public void update(){
    updateObjectPosition();
    if(active) {
      timer += 0.16f;
      float shakeFactor = (float) (amplitude * Math.max(0.1,Math.log(amount)));
      applyShake(shakeFactor);
    }
    
  }
  
  public double getXShake() {
    return x; 
  }
  
  public double getYShake() {
    return y;
  }
  
  private void updateObjectPosition() {
    x = this.gameObject.getX();
    y = this.gameObject.getY();
  }
  
 
}




