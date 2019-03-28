package shared.gameObjects.UI;

import client.main.Settings;
import javafx.scene.Group;
import shared.gameObjects.players.Player;

/**
 * The parent UI managing class
 */
public class UI {

  private PlayerInfo playerInfo;
  private Score score;
  private Settings settings;

  /**
   * Constructs a UI pertaining to a specific player
   *
   * @param root UI root to render to
   * @param clientPlayer Player to obtain data from
   */
  public UI(Group root, Player clientPlayer, Settings settings) {

    //Load the Kenney font
    //uiFont = Font.loadFont("fonts/kenney1.ttf", 25);
    settings = settings;
    playerInfo = new PlayerInfo(root, clientPlayer,settings);
    score = new Score(root, clientPlayer, settings);

  }

  /**
   * Renders the UI
   */
  public void render() {
    playerInfo.render();
    score.render();
  }


}
