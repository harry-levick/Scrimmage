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
public class Timer {

  private static final int xPos = 1920/2; //Center it on the screen.
  private static final int yPos = 25;
  private Animator board;
  private Player player;
  private ImageView boardImageView;
  private Text timerText;

  /**
   * Constructs a score UI pertaining to a player
   *
   * @param root UI root to render to
   */
  public Timer(Group root, Player clientPlayer) {
    player = clientPlayer;
    timerText = new Text(xPos + 20, yPos + 40, "");
    timerText.setFont(new Font(48));
    root.getChildren().addAll(this.boardImageView, this.timerText);
  }
  

  /**
   * Renders the timer UI
   */
  public void render() {
    //timerText.setText("Score:" + Integer.toString(player.getScore()));
    boardImageView.setImage(board.getImage());
  }

}
