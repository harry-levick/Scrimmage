package shared.gameObjects.UI;


import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class Score{

  private Animator animation;
  private Group root;
  private ImageView imageView;
  private Player player;
  
  private Text score;
  
  private final int xPos = (1920/2)-(149/2); //Center it on the screen.
  private final int yPos = 5;
  
  public Score(Group root, Player clientPlayer) {
    animation = new Animator();
    animation.supplyAnimation("default", "images/ui/score.png");
    imageView = new ImageView();
    root.getChildren().add(this.imageView);
    
    imageView.setX(xPos);
    imageView.setY(yPos);
    
    player = clientPlayer;
    score = new Text(xPos+55,yPos+102,"");
    score.setFont(new Font(72));
    root.getChildren().add(this.score);
  }
 

  public void render() {
    score.setText(Integer.toString(player.getScore()));
    imageView.setImage(animation.getImage());
  }

}