package shared.gameObjects.UI;


import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;

/**
 * UI container for the score of the player
 */
public class Score {

  private static final int xPos = 1920 - 247; //Center it on the screen.
  private static final int yPos = 0;
  private Animator board;
  private Player player;
  private ImageView boardImageView;
  private Text scoreText;

  /**
   * Constructs a score UI pertaining to a player
   * @param root UI root to render to
   */
  public Score(Group root, Player clientPlayer) {
    board = new Animator();
    board.supplyAnimation("default", "images/ui/score.png");
    boardImageView = new ImageView();


    boardImageView.setX(xPos);
    boardImageView.setY(yPos);

    player = clientPlayer;
    scoreText = new Text(xPos + 20, yPos + 40,"");
    scoreText.setFont(new Font(48));
    root.getChildren().addAll(this.boardImageView,this.scoreText);
  }


  /**
   * Renders the UI
   */
  public void render() {
    scoreText.setText("Score:"+Integer.toString(player.getScore()));
    boardImageView.setImage(board.getImage());
  }

}
