package shared.gameObjects.UI;

import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class PlayerInfo {
  
  private Animator board;
  private Group root;
  private Player clientPlayer;
  
  private ImageView boardImageView;
  
  public PlayerInfo(Group root, Player clientPlayer) {
    root = root;
    clientPlayer = clientPlayer;
    
    //Set the images/animations 
    board = new Animator();
    board.supplyAnimation("default","images/ui/board1.png");
    boardImageView = new ImageView();
    
    
    
    
    //Add to the root 
    root.getChildren().addAll(boardImageView);
  }
  
  
  public void render() {
    boardImageView.setImage(board.getImage());
  }

}
