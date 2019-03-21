package shared.gameObjects.UI;

import shared.gameObjects.animator.Animator;
import javafx.geometry.Rectangle2D;
import shared.gameObjects.players.Player;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

/**
 * UI container for the player data
 */
public class PlayerInfo {

  private Group root;
  private Player player;

  private Animator board;
  private Animator healthBar;
  private Animator ammoBar;

  private ImageView boardImageView;
  private ImageView healthBarImageView;
  private ImageView ammoBarImageView;

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
    board.supplyAnimation("default","images/ui/board1.png");
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

    //Add to the root
    root.getChildren().addAll(boardImageView,healthBarImageView,ammoBarImageView);
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
    float scale = (float) player.getHealth() / (float) 100;
    scale = 0.5f;
    if (scale > 0) { //Used so that the viewport doesn't reset to full when width = 0.
      int w = Math.round(scale * (float) ammoW);
      return new Rectangle2D(0, 0, w, ammoH);
    } else {
      return new Rectangle2D(0, 0, 1, ammoH);
    }
  }

  /**
   * Renders the UI
   */
  public void render() {
    boardImageView.setImage(board.getImage());

    healthBarImageView.setImage(healthBar.getImage());
    ammoBarImageView.setImage(ammoBar.getImage());

    healthBarImageView.setViewport(getHealthViewport());
    ammoBarImageView.setViewport(getAmmoViewport());


  }

}
