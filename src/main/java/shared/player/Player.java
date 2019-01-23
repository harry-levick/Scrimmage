package shared.player;

import javafx.scene.shape.*;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public class Player extends GameObject{
  
  Rectangle sprite;
  
  public Player(double x, double y, ObjectID id) {
    //y = 200 as a constant y position for the moment.
    super(x, 200, id);
    
    this.sprite = new Rectangle(this.x,this.y,200,200);
    // TODO Auto-generated constructor stub
  }
  
 
  // These are just temporary before physics gets implemented 
  
  public void moveLeft() {
    
  }
  
  public void moveRight() {
    
  }

  
  
  @Override
  public void update() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void render() {
    // TODO Auto-generated method stub
    
  }
  
  
  
  
  
}