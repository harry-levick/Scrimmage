package shared.gameObjects.background;

import javafx.scene.image.ImageView;
import shared.gameObjects.players.Player;

public class Parallax {
  
  ImageView background;
  Player player;
  double tx;
  double ty;
  
  public Parallax(ImageView backing, Player p) {
    background = backing;
    player = p;
    tx = 0;
    ty = 0;
  }
  
  private void move() {
    if(player.leftKey) {
      tx = -100;
    }
    else if(player.rightKey) {
      tx = 100;
    }
    else if(player.jumpKey) {
      ty = -100;
    }
  }
  
  public void update() {
    move();
    background.setTranslateX(player.getX()+tx);
    background.setTranslateY(player.getY()+ty);
  }
  
  
  
  
  
  
  
  
  

}
