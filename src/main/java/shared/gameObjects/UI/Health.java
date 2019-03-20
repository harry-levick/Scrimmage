package shared.gameObjects.UI;


import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;

public class Health {

  private final int WIDTH = 266;
  private final int HEIGHT = 63;
  private Animator animationForeground;
  private Animator animationBackground;
  private Group root;
  private ImageView foregroundImageView;
  private ImageView backgroundImageView;
  private Player player;
  private int xPos = 10;
  private int yPos = 20;

  public Health(Group root, Player clientPlayer) {
    animationForeground = new Animator();
    animationForeground.supplyAnimation("default", "images/ui/health/healthBar.png");
    animationBackground = new Animator();
    animationBackground.supplyAnimation("default", "images/ui/health/healthBacking.png");

    foregroundImageView = new ImageView();
    backgroundImageView = new ImageView();
    root.getChildren().addAll(this.backgroundImageView, this.foregroundImageView);

    foregroundImageView.setX(xPos);
    foregroundImageView.setY(yPos);

    backgroundImageView.setX(xPos);
    backgroundImageView.setY(yPos);

    player = clientPlayer;

  }

  private Rectangle2D calculateVP() {
    float scale = (float) player.getHealth() / (float) 100;
    if (scale > 0) { //Used so that the viewport doesn't reset to full when width = 0.
      int w = Math.round(scale * (float) WIDTH);
      return new Rectangle2D(0, 0, w, HEIGHT);
    } else {
      return new Rectangle2D(0, 0, 1, HEIGHT);
    }
  }


  public void render() {
    backgroundImageView.setImage(animationBackground.getImage());

    foregroundImageView.setViewport(calculateVP());
    foregroundImageView.setImage(animationForeground.getImage());


  }

}