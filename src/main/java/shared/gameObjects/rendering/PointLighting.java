package shared.gameObjects.rendering;

import javafx.scene.image.Image;

public class PointLighting{
  
  private Image overlay = new Image("images/activeLightingOverlay.png");
  
  private int centerX; 
  private int centerY;
  private int drawX;
  private int drawY;
  
  
  public PointLighting() {
    
  }
  
  
  public void update(double cx,double cy) {
    centerX = (int)cx;
    centerY = (int)cy;
    getNewPosition();
  }
  
  private void getNewPosition() {
    drawX = -1920 - (1920/2 - centerX);
    drawY = -1080 - (1080/2 - centerY);
  }
  
  public int getX() {
    return drawX;
  }
  public int getY() {
    return drawY;
  }
  
  public Image getImage() {
    return overlay; 
  }
 
}




  
  
  