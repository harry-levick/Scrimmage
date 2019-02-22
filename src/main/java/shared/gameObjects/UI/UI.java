package shared.gameObjects.UI;

import shared.gameObjects.players.Player;
import java.io.File;
import java.io.FileInputStream;
import javafx.scene.Group;
import javafx.scene.text.Font;

public class UI {
  
  private Health health; 
  private Ammo ammo; 
  private Score score;
  
 

  public UI(Group root, Player clientPlayer) {
    
    //Load the Kenney font 
    //uiFont = Font.loadFont("fonts/kenney1.ttf", 25);

    
    health = new Health(root,clientPlayer);
    ammo = new Ammo(root,clientPlayer); 
    score = new Score(root,clientPlayer);
  }
  
  public void render() {
    health.render();
    ammo.render();
    score.render();

  }
  
 
}
