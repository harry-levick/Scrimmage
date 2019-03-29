package shared.gameObjects.UI;

import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;
import javafx.scene.image.ImageView;
import javafx.scene.Group;

/**
 * The parent UI managing class
 */
public class PlayerPointer {

  private Animator upImage = new Animator(); 
  private Animator leftImage = new Animator();
  private Animator rightImage = new Animator();

  private transient ImageView imageView;
  
  private Player player;
  
  private String active = "no" ;
  private double activeX = 0;
  private double activeY = 0;
  private double playerW;
  private double playerH;
  private double playerX = 0;
  private double playerY = 0;
  
  public PlayerPointer(Group root,Player player) {
    this.player = player;
    imageView = new ImageView();
    playerW = player.getTransform().getSize().getX();
    playerH = player.getTransform().getSize().getY();
    
    upImage.supplyAnimation("default","images/ui/arrowUp.png");
    leftImage.supplyAnimation("default","images/ui/arrowLeft.png");
    rightImage.supplyAnimation("default","images/ui/arrowRight.png");
    root.getChildren().addAll(imageView);
  }
  
  public void update() {
    active = "no";
    playerX = player.getX();
    playerY = player.getY();
    
    if(playerX + playerW <0) {
      active = "right"; 
      activeX = 0;
      activeY = playerY + (playerH /2) - (leftImage.getImage().getHeight()/2);
    }
    else if(playerX > 1920) {
      active = "left"; 
      activeX = 1920 - rightImage.getImage().getWidth();
      activeY = playerY + (playerH / 2) - (leftImage.getImage().getHeight()/2);
    }
    else if(playerY + playerH < 0) {
      active = "up";
      activeX = playerX + (playerW /2) - (upImage.getImage().getWidth()/2);
      activeY = 0;
    }
  }
  
  public void render() {
    update();
    imageView.setImage(null);
    imageView.setX(activeX);
    imageView.setY(activeY);
    if(active.equals("left")) {
      imageView.setImage(leftImage.getImage());
    }
    else if(active.equals("right")) {
      imageView.setImage(rightImage.getImage());
    }
    else if(active.equals("up")) {
      imageView.setImage(upImage.getImage());
    }
  }

}
