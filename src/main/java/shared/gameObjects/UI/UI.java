package shared.gameObjects.UI;

import javafx.scene.Group;
import shared.gameObjects.players.Player;

public class UI {

  private PlayerInfo playerInfo;
  private Score score;


  public UI(Group root, Player clientPlayer) {

    //Load the Kenney font
    //uiFont = Font.loadFont("fonts/kenney1.ttf", 25);

    playerInfo = new PlayerInfo(root,clientPlayer);
    score = new Score(root, clientPlayer);
  }

  public void render() {
    playerInfo.render();
    //score.render();
  }


}
