package shared.gameObjects.UI;


import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.players.Player;


public class Ammo {

  private final int xPos = 1770;
  private final int yPos = 20;
  private Animator animation;
  private ImageView imageView;
  private Player player;
  private Text ammoAmount;
  private Text weapon;

  public Ammo(Group root, Player clientPlayer) {
    animation = new Animator();
    animation.supplyAnimation("default", "images/ui/ammo.png");
    imageView = new ImageView();
    root.getChildren().add(this.imageView);

    imageView.setX(xPos);
    imageView.setY(yPos);

    player = clientPlayer;
    weapon = new Text(xPos + 5, yPos + 58, "");
    ammoAmount = new Text(xPos + 35, yPos + 100, "");
    weapon.setFont(new Font(19));
    ammoAmount.setFont(new Font(40));

    root.getChildren().addAll(this.weapon, this.ammoAmount);


  }


  private String getWeapon() {
    if (player.getHolding() != null) {
      String returnText = player.getHolding().getName();
      return returnText.substring(0, returnText.indexOf('@'));
    }
    return "Empty handed!";
  }


  private String getAmmoCount() {
    if (player.getHolding() != null) {
      return Integer.toString(player.getHolding().getAmmo());
    }
    return "0";
  }


  public void render() {
    ammoAmount.setText(getAmmoCount());
    weapon.setText(getWeapon());
    imageView.setImage(animation.getImage());
  }

}