package shared.gameObjects.UI;


import java.util.Timer;
import client.main.Client;
import client.main.Settings;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;

/**
 * UI container for the score of the player
 */
public class TimeUI {

  private static final int xPos = 1920 / 2; //Center it on the screen.
  private static final int yPos = 25;
  private Animator board;
  private Player player;
  //private ImageView boardImageView;
  private Text timerText;
  private int timeRemaining;

  /**
   * Constructs a score UI pertaining to a player
   *
   * @param root UI root to render to
   */
  public TimeUI(Group root, Player clientPlayer,Settings settings) {
    player = clientPlayer;
    timeRemaining = timeRemaining;
    timerText = new Text(xPos, yPos, "");
    timerText.setFont(settings.getFont(42));
    root.getChildren().addAll(this.timerText);
  }


  /**
   * Renders the timer UI
   */
  public void render() {
    timerText.setText(Integer.toString(Client.timeRemaining));
    //boardImageView.setImage(board.getImage());
  }

}
