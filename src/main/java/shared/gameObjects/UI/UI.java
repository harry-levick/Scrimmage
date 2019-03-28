package shared.gameObjects.UI;

import javafx.scene.Group;
import shared.gameObjects.players.Player;

/**
 * The parent UI managing class
 */
public class UI {

  private PlayerInfo playerInfo;
  private Score score;

  /**
   * Constructs a UI pertaining to a specific player
   *
   * @param root UI root to render to
   * @param clientPlayer Player to obtain data from
   */
  public UI(Group root, Player clientPlayer) {

    //Load the Kenney font
    //uiFont = Font.loadFont("fonts/kenney1.ttf", 25);

    playerInfo = new PlayerInfo(root, clientPlayer);
    score = new Score(root, clientPlayer);
  }

  /**
   * Renders the UI
   */
  public void render() {
    playerInfo.render();
    score.render();
  }


}
