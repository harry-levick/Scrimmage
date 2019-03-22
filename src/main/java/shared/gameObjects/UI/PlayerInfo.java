package shared.gameObjects.UI;

import shared.gameObjects.animator.Animator;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import shared.gameObjects.players.Player;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * UI container for the player data
 */
public class PlayerInfo {

  private Group root;
  private Player player;

  private Animator board;
  private Animator board2;
  private Animator healthBar;
  private Animator ammoBar;

  private ImageView boardImageView;
  private ImageView healthBarImageView;
  private ImageView ammoBarImageView;
  
  private Text ammoText;

  private double healthW;
  private double healthH;
  private double ammoW;
  private double ammoH;

  /**
   * Constructs UI pertaining to player
   * @param root UI root to render to
   */
  public PlayerInfo(Group root, Player clientPlayer) {
    root = root;
    player = clientPlayer;

    //Set the images/animations
    board = new Animator();
    board2 = new Animator();
    board.supplyAnimation("default","images/ui/board1.png");
    board2.supplyAnimation("default","images/ui/board2.png");
    boardImageView = new ImageView();

    healthBar = new Animator();
    healthBar.supplyAnimation("default","images/ui/healthBar.png");
    healthBarImageView = new ImageView();
    healthW = healthBar.getImage().getWidth();
    healthH = healthBar.getImage().getHeight();


    ammoBar = new Animator();
    ammoBar.supplyAnimation("default","images/ui/ammo.png");
    ammoBarImageView = new ImageView();
    ammoW = ammoBar.getImage().getWidth();
    ammoH = ammoBar.getImage().getHeight();
    ammoText = new Text(20, 100,"");
    ammoText.setFont(new Font(48));

    //Add to the root
    root.getChildren().addAll(boardImageView,healthBarImageView,ammoBarImageView,ammoText);
  }


  private Rectangle2D getHealthViewport() {
    float scale = (float) player.getHealth() / (float) 100;
    if (scale > 0) { //Used so that the viewport doesn't reset to full when width = 0.
      int w = Math.round(scale * (float) healthW);

      return new Rectangle2D(0, 0, w, healthH);
    } else {
      return new Rectangle2D(0, 0, 1, healthH);
    }
  }

  private Rectangle2D getAmmoViewport() {
    float playerAmmo = (float)player.getHolding().getAmmo();
    if(playerAmmo != -1) {
      float scale = (float) player.getHolding().getAmmo() / (float)player.getHolding().getMaxAmmo();
      if (scale > 0) { //Used so that the viewport doesn't reset to full when width = 0.
        int w = Math.round(scale * (float) ammoW);
        return new Rectangle2D(0, 0, w, ammoH);
      } else {
        return new Rectangle2D(0, 0, 1, ammoH);
      }
    }
    else {
      return new Rectangle2D(0,0,1,1);
    }
  }
  
  private Image getBoardImage() {
    if (player.getHolding().getAmmo() == -1){
      return board2.getImage();
    }
    else {
      return board.getImage();
    }
  }
  
  private String getAmmoText() {
    if(player.getHolding().getAmmo() != -1) {
      return Integer.toString(player.getHolding().getAmmo())+" / "+Integer.toString(player.getHolding().getMaxAmmo());
    }
    else {
      return "";
    }
  }

  /**
   * Renders the UI
   */
  public void render() {
    boardImageView.setImage(getBoardImage());

    healthBarImageView.setImage(healthBar.getImage());
    ammoBarImageView.setImage(ammoBar.getImage());

    healthBarImageView.setViewport(getHealthViewport());
    ammoBarImageView.setViewport(getAmmoViewport());
    
    ammoText.setText(getAmmoText());


  }

}
